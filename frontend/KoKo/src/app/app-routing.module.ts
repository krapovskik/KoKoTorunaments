import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthComponent} from "./components/auth/auth.component";
import {LoginComponent} from "./components/auth/login/login.component";
import {ActivateAccountComponent} from "./components/auth/activate-account/activate-account.component";
import {HomeComponent} from "./components/home/home.component";
import {AdminComponent} from "./components/admin/admin.component";
import {OrganizerRequestsComponent} from "./components/admin/organizer-requests/organizer-requests.component";
import {OrganizersComponent} from "./components/admin/organizers/organizers.component";
import {AuthGuard} from "./helper/auth.guard";
import {TournamentComponent} from "./components/tournament/tournament.component";

const routes: Routes = [
    {
        path: '',
        component: HomeComponent
    },
    {
        path: '',
        component: AuthComponent,
        children: [
            {
                path: 'login',
                component: LoginComponent
            },
            {
                path: 'activate',
                component: ActivateAccountComponent
            }
        ]
    },
    {
        path: 'admin',
        component: AdminComponent,
        canActivate: [AuthGuard],
        data: {role: 'ADMIN'},
        children: [
            {
                path: 'organizerRequests/:currentPage/:size',
                component: OrganizerRequestsComponent
            },
            {
                path: 'organizers/:currentPage/:size',
                component: OrganizersComponent
            }
        ]
    },
    {
        path: 'tournaments',
        component: TournamentComponent
    },
    {
        path: '**',
        component: HomeComponent
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
