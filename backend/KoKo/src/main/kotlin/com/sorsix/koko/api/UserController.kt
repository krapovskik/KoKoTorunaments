package com.sorsix.koko.api

import com.sorsix.koko.dto.request.RequestOrganizerRequest
import com.sorsix.koko.dto.request.SendInviteRequest
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.service.AppUserService
import com.sorsix.koko.util.MapperService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(val appUserService: AppUserService, val mapperService: MapperService) {

    @PostMapping("/requestOrganizer")
    fun requestOrganizer(@RequestBody requestOrganizer: RequestOrganizerRequest): ResponseEntity<out Response> {
        val result = appUserService.saveOrganizerRequest(requestOrganizer)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @GetMapping("/searchUser")
    fun searchUser(@RequestParam query: String?) = appUserService.searchUser(query)

    @PostMapping("/sendInvite")
    fun sendInvite(@RequestBody sendInviteRequest: SendInviteRequest): ResponseEntity<out Response> {
        val result = appUserService.sendInvite(sendInviteRequest)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @PutMapping("/changeProfilePhoto")
    fun updateProfilePhoto(@RequestBody image: String) =
        ResponseEntity.ok(this.appUserService.updateProfilePhoto(image))
}
