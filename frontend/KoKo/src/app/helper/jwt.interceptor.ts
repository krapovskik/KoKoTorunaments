import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {TokenService} from "../service/token.service";

@Injectable({
    providedIn: "root"
})
export class JwtInterceptor implements HttpInterceptor {

    constructor(private tokenService: TokenService) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let currentUser = this.tokenService.getUser();
        let isLoggedIn = currentUser && currentUser.token;
        if (isLoggedIn) {
            request = request.clone({
                setHeaders: {
                    Authorization: `Bearer ${currentUser?.token}`
                }
            });
        }
        return next.handle(request);
    }
}
