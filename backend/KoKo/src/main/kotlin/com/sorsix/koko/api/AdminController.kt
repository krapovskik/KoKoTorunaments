package com.sorsix.koko.api

import com.sorsix.koko.dto.response.BadRequestResponse
import com.sorsix.koko.dto.response.NotFoundResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.dto.response.SuccessResponse
import com.sorsix.koko.service.AdminService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(val adminService: AdminService) {

    @GetMapping("/organizerRequests")
    fun getOrganizerRequests(pageable: Pageable) = adminService.getAllOrganizerRequestsPaginated(pageable)

    @PutMapping("/approveRequest")
    fun approveRequest(@RequestBody requestId: Long): ResponseEntity<Response> {
        return when (val result = adminService.approveRequest(requestId)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }
    }

    @PutMapping("/rejectRequest")
    fun rejectRequest(@RequestBody requestId: Long): ResponseEntity<Response> {
        return when (val result = adminService.rejectRequest(requestId)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }
    }

    @GetMapping("/organizers")
    fun getAllOrganizers(pageable: Pageable) = adminService.getAllOrganizers(pageable)

    @PutMapping("/revokeOrganizer")
    fun revokeOrganizer(@RequestBody userId: Long): ResponseEntity<Response> {
        return when (val result = adminService.revokeOrganizer(userId)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }
    }
}
