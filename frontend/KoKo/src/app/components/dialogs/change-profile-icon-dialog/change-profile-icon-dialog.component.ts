import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MessageService} from "../../../service/message.service";
import {UserService} from "../../../service/user.service";

@Component({
    selector: 'app-change-profile-icon-dialog',
    templateUrl: './change-profile-icon-dialog.component.html',
    styleUrls: ['./change-profile-icon-dialog.component.css']
})
export class ChangeProfileIconDialogComponent implements OnInit {

    profileImages = ['profile0.jpg', 'profile1.jpg', 'profile2.jpg',
        'profile3.jpg', 'profile4.jpg', 'profile5.jpg',
        'profile6.jpg', 'profile7.jpg', 'profile8.jpg']

    imagesWithSelectProperty: { image: string, selected: boolean }[] = []

    constructor(
        private dialogRef: MatDialogRef<ChangeProfileIconDialogComponent>,
        private messageService: MessageService,
        private userService: UserService,
        @Inject(MAT_DIALOG_DATA) public profilePhoto: string,
    ) {
    }

    ngOnInit(): void {
        this.imagesWithSelectProperty = this.profileImages.map(image => {
            return {
                image: image,
                selected: this.profilePhoto == image
            }
        })
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    onChange() {
        let obj = this.imagesWithSelectProperty.filter(obj => obj.selected)[0]
        this.userService.updateProfilePhoto(obj.image)
            .subscribe({
                next: data => {
                    this.messageService.showSuccessMessage(data.response)
                    this.dialogRef.close("success")
                }
            })
    }

    changeSelected(image: string) {
        this.imagesWithSelectProperty.forEach(obj => obj.selected = obj.image == image)
    }
}
