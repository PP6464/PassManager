package backend

object Validator {
	private val emailRegex = Regex("^[\\w.\\-+]+@\\w+\\.[\\w.]+$")
	private val domainRegex = Regex("^\\w+\\.[\\w.]+$")
	private val alphanumericCharsRegex = Regex("[a-zA-Z0-9]")
	private val numberRegex = Regex("\\d")
	private val noneCapsRegex = Regex("[^A-Z]")
	private val specialCharList = charArrayOf('~', '?', '@', '#', '\'', '"', '$', '&', '*', '(', ')', '!', ':', ';', '.', '<', '>', '{', '}', '[', ']', '|', '\\', '/', ',', '-', '+', '=')
	private val numberList = ('0'..'9').toList()
	private val lowerCaseList = ('a'..'z').toList()
	private val upperCaseList = ('A'..'Z').toList()
	
	fun isValidEmail(email: String) : Boolean {
		return emailRegex.matches(email)
	}
	
	fun isValidPassword(password: String) : Boolean {
		if (password.all { alphanumericCharsRegex.matches(it.toString()) }) return false // No special chars
		if (!password.any { numberRegex.matches(it.toString()) }) return false // No digits
		if (password.all { noneCapsRegex.matches(it.toString()) }) return false // No caps chars
		if (password.length < 10) return false // Too short
		return true
	}
	
	fun isValidDomain(domain: String) = domainRegex.matches(domain)
	
	fun generateRandomPassword() : String {
		val length = (10..100).random()
		
		val nSpecial = (1..length/4).random()
		val nLower = (1..length/4).random()
		val nUpper = (1..length/4).random()
		val nNumber = length - nSpecial - nLower - nUpper
		
		val specials = (1..nSpecial).map { specialCharList.random() }.toMutableList()
		val lowers = (1..nLower).map { lowerCaseList.random() }.toMutableList()
		val uppers = (1..nUpper).map { upperCaseList.random() }.toMutableList()
		val numbers = (1..nNumber).map { numberList.random() }.toMutableList()
		
		val charsLists = listOf(specials, lowers, uppers, numbers)
		
		var password = ""
		
		repeat(length) {
			var typeIndex = (0..3).random()
			
			while (charsLists[typeIndex].isEmpty()) {
				typeIndex += 1
				typeIndex %= 4
			}
			
			password += charsLists[typeIndex].removeFirst()
		}
		
		return password
	}
}