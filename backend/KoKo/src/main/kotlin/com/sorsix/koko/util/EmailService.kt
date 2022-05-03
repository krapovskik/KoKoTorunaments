package com.sorsix.koko.util

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(val mailSender: JavaMailSender) {

    private val fromMail = "kokotournaments@outlook.com"

    fun sendNewAccountMail(to: String, token: String) {
        val subject = "KoKo Tournaments account activation"
        val text = """Finish creating your account on the following link
            |http://localhost:4200/activate?token=${token}""".trimMargin()

        sendEmail(to, subject, text)
    }

    fun sendInviteEmail(to: String, teamName: String, invitedBy: String, token: String) {
        val subject = "KoKo Tournaments team invitation"
        val text = """You have been invited by $invitedBy to join $teamName.
            |Join on the following link
            |http://localhost:4200/activate?token=${token}""".trimMargin()

        sendEmail(to, subject, text)
    }

    private fun sendEmail(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()

        message.setFrom(fromMail)
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)

        mailSender.send(message)
    }
}