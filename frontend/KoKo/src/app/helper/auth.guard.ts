import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {TokenService} from "../service/token.service";

@Injectable({
    providedIn: "root"
})
export class AuthGuard implements CanActivate {

    constructor(
        private tokenService: TokenService,
        private router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
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
