package backend

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import io.appwrite.services.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Properties

object Appwrite {
	private var client: Client? = null
	var currentUser: User<Map<String, Any>>? = null
	private var currentAccount : Account? = null
	
	fun initAppwrite() {
		val properties = Properties().apply {
			load(Appwrite.javaClass.classLoader!!.getResourceAsStream("appwrite.properties"))
		}
		
		val keySecret = properties.getProperty("APPWRITE_KEY_SECRET")
		val projectId = properties.getProperty("APPWRITE_PROJECT_ID")
		
		client = Client()
			.setKey(keySecret)
			.setProject(projectId)
	}
	
	private val users
		get() = Users(client!!)
	
	fun userExists(email: String, callback: (Result<Boolean>) -> Unit) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val res = users.list(
					queries = listOf(Query.equal("email", email))
				)
				
				val usr = res.users.firstOrNull()
				
				if (usr == null) {
					callback(Result.failure(Exception("User not found")))
				} else {
					callback(Result.success(true))
				}
			} catch (e: Exception) {
				callback(Result.failure(e))
			}
		}
	}
	
	fun login(email: String, password: String, callback: (Result<User<Map<String, Any>>>) -> Unit) {
		val account = Account(client!!)
		
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val s = account.createEmailPasswordSession(email, password)
				
				currentAccount = account
				currentUser = Users(client!!).get(s.userId)
				
				callback(Result.success(currentUser!!))
			} catch (e: AppwriteException) {
				callback(Result.failure(e))
			}
		}
	}
	
	fun createUser(email: String, password: String, callback: (Result<User<Map<String, Any>>>) -> Unit) {
		val account = Account(client!!)
		
		CoroutineScope(Dispatchers.IO).launch {
			try {
				currentUser = account.create(
					userId = ID.unique(),
					email = email,
					password = password,
				)
				
				currentAccount = account
				
				callback(Result.success(currentUser!!))
			} catch (e: AppwriteException) {
				callback(Result.failure(e))
			}
		}
	}
}