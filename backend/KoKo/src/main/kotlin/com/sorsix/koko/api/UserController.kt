package com.sorsix.koko.api

import com.sorsix.koko.dto.request.RegisterRequest
import com.sorsix.koko.dto.request.RequestOrganizerRequest
import com.sorsix.koko.dto.request.SendInviteRequest
import com.sorsix.koko.dto.response.*
import com.sorsix.koko.service.AppUserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(val appUserService: AppUserService) {

    @PostMapping("/requestOrganizer")
    fun requestOrganizer(@RequestBody requestOrganizer: RequestOrganizerRequest): ResponseEntity<Response> {
        return when (val result = appUserService.saveOrganizerRequest(requestOrganizer)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }
    }

    @GetMapping("/searchUser")
    fun searchUser(@RequestParam query: String?): List<TeamMemberResponse> {
        return appUserService.searchUser(query)
    }

    @PostMapping("/sendInvite")
    fun sendInvite(@RequestBody sendInviteRequest: SendInviteRequest): ResponseEntity<Response> {
        return when (val result = appUserService.sendInvite(sendInviteRequest)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }
    }

    @PutMapping("/changeProfilePhoto")
    fun updateProfilePhoto(@RequestBody image: String): ResponseEntity<Response> =
        ResponseEntity.ok(this.appUserService.updateProfilePhoto(image))
}
