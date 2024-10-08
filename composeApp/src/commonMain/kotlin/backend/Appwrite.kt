package backend

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Document
import io.appwrite.models.User
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Properties

object Appwrite {
	private var client: Client? = null
	var currentUser: User<Map<String, Any>>? = null
	private var currentAccount: Account? = null
	
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
	
	fun createUser(
		email: String,
		password: String,
		callback: (Result<User<Map<String, Any>>>) -> Unit
	) {
		val account = Account(client!!)
		
		CoroutineScope(Dispatchers.IO).launch {
			try {
				currentUser = account.create(
					userId = ID.unique(),
					email = email,
					password = password,
				)
				
				currentAccount = account
				
				Databases(client!!)
					.createCollection(
						databaseId = "passwords",
						name = currentUser!!.id,
						collectionId = currentUser!!.id,
					)
				
				callback(Result.success(currentUser!!))
			} catch (e: AppwriteException) {
				callback(Result.failure(e))
			}
		}
	}
	
	suspend fun changeProfile(email: String, password: String?, callback: (Result<Unit>) -> Unit) {
		try {
			if (email != currentUser!!.email) {
				Users(client!!).updateEmail(
					userId = currentUser!!.id,
					email = email,
				)
			}
			if (password != null) {
				try {
					Users(client!!).updatePassword(
						userId = currentUser!!.id,
						password = password,
					)
				} catch (e: AppwriteException) {
					if (!e.message!!.contains("target")) {
						e.printStackTrace()
					}
				}
			}
			currentUser = Users(client!!).get(currentUser!!.id) // Update user's email and password in local variable
			
			callback(Result.success(Unit))
		} catch (e: AppwriteException) {
			callback(Result.failure(e))
		}
	}
	
	suspend fun fetchPasswords(callback: (Result<List<Password>>) -> Unit) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val res = Databases(client!!).listDocuments(
					databaseId = "passwords",
					collectionId = currentUser!!.id,
				)
				val passwords = res.documents.map { Password.fromMap(it.id, it.data) }
				callback(Result.success(passwords))
			} catch (e: Exception) {
				callback(Result.failure(e))
			}
		}
	}
	
	suspend fun getPassword(id: String, callback: (Result<Password>) -> Unit) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				val res = Databases(client!!).getDocument("passwords", currentUser!!.id, id)
				callback(Result.success(Password.fromMap(id, res.data)))
			} catch (e: Exception) {
				callback(Result.failure(e))
			}
		}
	}
	
	suspend fun createPassword(domain: String, password: String, callback: (Result<Document<Map<String, Any>>>) -> Unit) {
		try {
			val doc = Databases(client!!).createDocument(
				databaseId = "passwords",
				collectionId = currentUser!!.id,
				data = mapOf(
					"domain" to domain,
					"password" to password,
				),
				documentId = ID.unique(),
			)
			
			callback(Result.success(doc))
		} catch (e: Exception) {
			callback(Result.failure(e))
		}
	}
	
	suspend fun setPassword(id: String, newPassword: String, callback: (Result<Unit>) -> Unit) {
		CoroutineScope(Dispatchers.IO).launch {
			try {
				Databases(client!!).updateDocument(
					databaseId = "passwords",
					collectionId = currentUser!!.id,
					documentId = id,
					data = mapOf(
						"password" to newPassword,
					),
				)
				
				callback(Result.success(Unit))
			} catch (e: Exception) {
				callback(Result.failure(e))
			}
		}
	}
	
	suspend fun deletePassword(id: String, callback: (Result<Unit>) -> Unit) {
		try {
			Databases(client!!).deleteDocument(
				databaseId = "passwords",
				collectionId = currentUser!!.id,
				documentId = id,
			)
			
			callback(Result.success(Unit))
		} catch (e: Exception) {
			callback(Result.failure(e))
		}
	}
}

data class Password(val domain: String, var password: String, val id: String) {
	companion object {
		fun fromMap(id: String, map: Map<String, Any>) : Password {
			return Password(domain = map["domain"] as String, password = map["password"] as String, id = id)
		}
	}
}