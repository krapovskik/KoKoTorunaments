import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthComponent} from "./components/auth/auth.component";
import {LoginComponent} from "./components/auth/login/login.component";
import {ActivateAccountComponent} from "./components/auth/activate-account/activate-account.component";

const routes: Routes = [
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
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
