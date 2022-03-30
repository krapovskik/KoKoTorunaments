package com.sorsix.koko.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(val mailSender: JavaMailSender) {

    private val fromMail = "kokotournaments@outlook.com"

    fun sendNewAccountMail(to: String, token: String) {

        val message = SimpleMailMessage()

        message.setFrom(fromMail)
        message.setTo(to)
        message.setSubject("KoKo Tournaments account activation")

        val text = """Finish creating your account on the following link
            |http://localhost:4200/activate?token=${token}""".trimMargin()
        message.setText(text)

        mailSender.send(message)
    }
}