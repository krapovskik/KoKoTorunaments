import {Injectable} from "@angular/core";
import {Subject} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class MessageService {

    $successMessages = new Subject<string>()
    $errorMessages = new Subject<string>()

    showSuccessMessage(message: string) {
        this.$successMessages.next(message)
    }

    showErrorMessage(message: string) {
        this.$errorMessages.next(message)
    }
}
