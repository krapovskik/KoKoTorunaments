package com.sorsix.koko.service

import com.sorsix.koko.domain.enumeration.AppUserRole
import com.sorsix.koko.dto.response.*
import com.sorsix.koko.repository.AppUserRepository
import com.sorsix.koko.repository.OrganizerRequestRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AdminService(
    val organizerRequestRepository: OrganizerRequestRepository,
    val appUserRepository: AppUserRepository
) {

    fun getAllOrganizerRequestsPaginated(pageable: Pageable): Page<OrganizerRequestResponse> {
        val organizerRequests = organizerRequestRepository.findAll(pageable)
        val list = organizerRequests.content.map {
            OrganizerRequestResponse(it.id, "${it.user.firstName} ${it.user.lastName}", it.title, it.description)
        }

        return PageImpl(list, pageable, organizerRequests.totalElements)
    }

    @Transactional
    fun approveRequest(requestId: Long): Response {
        val request = organizerRequestRepository.findByIdOrNull(requestId)

        request?.let {
            val appUser = request.user
            appUserRepository.updateAppUserRole(appUser.id, AppUserRole.ORGANIZER)
            organizerRequestRepository.deleteById(requestId)
            return SuccessResponse("Organizer privileges granted")

        } ?: return NotFoundResponse("Organizer request not found")
    }

    fun rejectRequest(requestId: Long): Response {
        val request = organizerRequestRepository.findByIdOrNull(requestId)

        request?.let {
            organizerRequestRepository.deleteById(requestId)
            return SuccessResponse("Organizer request rejected")

        } ?: return NotFoundResponse("Organizer request not found")
    }

    fun getAllOrganizers(pageable: Pageable): Page<OrganizerResponse> {
        val organizers = appUserRepository.findAllByAppUserRole(pageable, AppUserRole.ORGANIZER)
        val list = organizers.content.map {
            OrganizerResponse(it.id, "${it.firstName} ${it.lastName}")
        }

        return PageImpl(list, pageable, organizers.totalElements)
    }

    @Transactional
    fun revokeOrganizer(userId: Long) = if (appUserRepository.updateAppUserRole(userId, AppUserRole.PLAYER) == 1) {
        SuccessResponse("Organizer privileges removed")
    } else {
        NotFoundResponse("User not found")
    }
}
