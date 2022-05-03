package com.sorsix.koko.api

import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.service.AdminService
import com.sorsix.koko.util.MapperService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController(val adminService: AdminService, val mapperService: MapperService) {

    @GetMapping("/organizerRequests")
    fun getOrganizerRequests(pageable: Pageable) = adminService.getAllOrganizerRequestsPaginated(pageable)

    @PutMapping("/approveRequest")
    fun approveRequest(@RequestBody requestId: Long): ResponseEntity<out Response> {
        val result = adminService.approveRequest(requestId)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @PutMapping("/rejectRequest")
    fun rejectRequest(@RequestBody requestId: Long): ResponseEntity<out Response> {
        val result = adminService.rejectRequest(requestId)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @GetMapping("/organizers")
    fun getAllOrganizers(pageable: Pageable) = adminService.getAllOrganizers(pageable)

    @PutMapping("/revokeOrganizer")
    fun revokeOrganizer(@RequestBody userId: Long): ResponseEntity<out Response> {
        val result = adminService.revokeOrganizer(userId)
        return mapperService.mapResponseToResponseEntity(result)
    }
}
