package backend

object Validator {
	private val emailRegex = Regex("^[\\w.\\-+]+@\\w+\\.[\\w.]+$")
	private val alphanumericCharsRegex = Regex("[a-zA-Z0-9]")
	private val numberRegex = Regex("\\d")
	private val noneCapsRegex = Regex("[^A-Z]")
	
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
}