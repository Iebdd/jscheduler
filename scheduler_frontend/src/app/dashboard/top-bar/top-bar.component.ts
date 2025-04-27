import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../Services/user.service';
import { User } from '../../model/interfaces';
import { InfoStates } from '../../model/enums';

/**
 * Provides the top bar, the top part of the dashboard
 */
@Component({
  selector: 'app-top-bar',
  imports: [],
  templateUrl: './top-bar.component.html',
  styleUrl: './top-bar.component.scss'
})
export class TopBarComponent  implements OnInit{

  private router: Router = inject(Router);
  private _userService: UserService = inject(UserService);
  protected _active_user: User = {
    user_id: '',
    role: -1,
    firstName: '',
    lastName: '',
    email: ''
  }

  setManagement() {
    this._userService.setInfoState(InfoStates.Management)
  }

  logOut() {
    this._userService.clearData();
    this._userService.setInfoState(InfoStates.Default);
    this.router.navigate(['/login']);
  }

  loadUserData() {
    this._userService.getUserData()
      .subscribe(user => this._active_user = user);
  }

  ngOnInit() {
    this.loadUserData();
  }
}
