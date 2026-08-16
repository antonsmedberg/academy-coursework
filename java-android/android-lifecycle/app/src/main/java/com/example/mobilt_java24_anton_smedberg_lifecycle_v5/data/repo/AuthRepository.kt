package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.repo

import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.db.UserDao
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.model.UserCredential
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.util.Hashing

class AuthRepository(private val userDao: UserDao) {
    suspend fun register(username: String, email: String, pnr: String, password: String): Result<Long> {
        // Jag dubbelkollar baskrav här – UI validerar redan.
        if (username.isBlank() || email.isBlank() || pnr.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("Ogiltig input"))
        }
        val salt = Hashing.randomSalt()
        val hash = Hashing.hashPassword(password, salt)
        val id = userDao.insert(UserCredential(username = username, email = email, personnummer = pnr, salt = salt, passwordHash = hash))
        return Result.success(id)
    }

    suspend fun login(username: String, password: String): Result<Long> {
        val user = userDao.findByUsername(username) ?: return Result.failure(IllegalArgumentException("Användare finns ej"))
        val hash = Hashing.hashPassword(password, user.salt)
        return if (hash == user.passwordHash) Result.success(user.id) else Result.failure(IllegalArgumentException("Fel lösenord"))
    }

    suspend fun ensureSeedUser(): Long {
        // Jag skapar en demo‑user om DB är tom → uppfyller "hårdkodad credentials ok".
        if (userDao.count() > 0) return -1
        val salt = Hashing.randomSalt()
        val pass = "password123"
        val id = userDao.insert(
            UserCredential(
                username = "demo",
                email = "demo@example.com",
                personnummer = "19900101-1234",
                salt = salt,
                passwordHash = Hashing.hashPassword(pass, salt)
            )
        )
        return id
    }
}