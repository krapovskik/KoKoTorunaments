import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable} from "rxjs";
import {TokenService} from "../service/token.service";

@Injectable({
    providedIn: "root"
})
export class AuthGuard implements CanActivate {

    constructor(private tokenService: TokenService, private router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        let currentUser = this.tokenService.getUser();
        if (currentUser) {
            let requiredRole = route.data['role'];
            if (requiredRole && requiredRole !== currentUser.role) {
                this.router.navigate(['/']);
                return false;
            }

            return true;
        }

        this.router.navigate(['/login']);
        return false;
    }
}
