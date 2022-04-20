import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CommonModule} from "@angular/common";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatMenuModule} from "@angular/material/menu";
import {MatListModule} from "@angular/material/list";
import {MatBadgeModule} from "@angular/material/badge";
import {RouterModule} from "@angular/router";
import {ScrollingModule} from "@angular/cdk/scrolling";
import {MatSelectModule} from "@angular/material/select";
import {MatCardModule} from "@angular/material/card";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {FlexLayoutModule} from "@angular/flex-layout"
import {MatChipsModule} from "@angular/material/chips";
import {AuthComponent} from './components/auth/auth.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatNativeDateModule, MatOptionModule, MatRippleModule} from "@angular/material/core";
import {MatInputModule} from "@angular/material/input";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {LoginComponent} from "./components/auth/login/login.component";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {ActivateAccountComponent} from './components/auth/activate-account/activate-account.component';
import {MatDialogModule} from "@angular/material/dialog";
import {RegisterUserDialog} from "./components/header/registerUserDialog/register-user-dialog.component";
import {HomeComponent} from './components/home/home.component';
import {AdminComponent} from './components/admin/admin.component';
import {SidebarComponent} from './components/admin/sidebar/sidebar.component';
import {MatSidenavModule} from "@angular/material/sidenav";
import {OrganizerRequestsComponent} from './components/admin/organizer-requests/organizer-requests.component';
import {AdminHeaderComponent} from "./components/admin/admin-header/admin-header.component";
import {OrganizersComponent} from './components/admin/organizers/organizers.component';
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatExpansionModule} from "@angular/material/expansion";
import {
    BecomeOrganizerDialogComponent
} from './components/header/become-organizer-dialog/become-organizer-dialog.component';
import {JwtInterceptor} from "./helper/jwt.interceptor";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {PaginationComponent} from './components/pagination/pagination.component';
import {TournamentsComponent} from './components/tournaments/tournaments.component';
import {LoadingButtonComponent} from "./components/loading-button/loading-button.component";
import { AllTournamentsComponent } from './components/all-tournaments/all-tournaments.component';
import { CreateTeamDialogComponent } from './components/header/create-team-dialog/create-team-dialog.component';
import { MyTeamsComponent } from './components/my-teams/my-teams.component';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import { JoinTournamentDialogComponent } from './components/tournaments/joinToutnamentDialog/join-tournament-dialog.component';
import { SendInviteDialogComponent } from './components/header/send-invite-dialog/send-invite-dialog.component';
import { TournamentComponent } from './components/tournament/tournament.component';
import { TournamentSideBarComponent } from './components/tournament/tournament-side-bar/tournament-side-bar.component';
import { ChangeScoreDialogComponent } from './components/tournament/change-score-dialog/change-score-dialog.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import { CreateTournamentDialogComponent } from './components/header/create-tournament-dialog/create-tournament-dialog.component';
import {MatDatepickerModule} from "@angular/material/datepicker";
import { WinnerDialogComponent } from './components/tournament/winner-dialog/winner-dialog.component';
import { MyProfileComponent } from './components/my-profile/my-profile.component';
import {MatGridListModule} from "@angular/material/grid-list";

declare global {
    interface Window {
        bracketsViewer?: any | undefined;
    }
}

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        AuthComponent,
        LoginComponent,
        ActivateAccountComponent,
        RegisterUserDialog,
        HomeComponent,
        AdminComponent,
        SidebarComponent,
        OrganizerRequestsComponent,
        AdminHeaderComponent,
        OrganizersComponent,
        BecomeOrganizerDialogComponent,
        PaginationComponent,
        TournamentsComponent,
        LoadingButtonComponent,
        AllTournamentsComponent,
        CreateTeamDialogComponent,
        MyTeamsComponent,
        JoinTournamentDialogComponent,
        SendInviteDialogComponent,
        TournamentComponent,
        TournamentSideBarComponent,
        ChangeScoreDialogComponent,
        CreateTournamentDialogComponent,
        WinnerDialogComponent,
        MyProfileComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        CommonModule,
        HttpClientModule,
        MatToolbarModule,
        MatIconModule,
        MatButtonModule,
        MatMenuModule,
        MatListModule,
        RouterModule,
        MatCardModule,
        ScrollingModule,
        MatBadgeModule,
        MatSelectModule,
        FormsModule,
        FlexLayoutModule,
        MatChipsModule,
        MatButtonModule,
        MatFormFieldModule,
        MatCardModule,
        ReactiveFormsModule,
        MatProgressSpinnerModule,
        MatInputModule,
        MatOptionModule,
        MatSelectModule,
        MatSnackBarModule,
        MatDialogModule,
        MatSidenavModule,
        MatPaginatorModule,
        MatExpansionModule,
        MatProgressBarModule,
        MatAutocompleteModule,
        MatRippleModule,
        MatCheckboxModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatGridListModule,
    ],
    providers: [
        {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
