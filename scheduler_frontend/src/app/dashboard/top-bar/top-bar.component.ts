import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../Services/user.service';

@Component({
  selector: 'app-top-bar',
  imports: [],
  templateUrl: './top-bar.component.html',
  styleUrl: './top-bar.component.scss'
})
export class TopBarComponent {

  private router: Router = inject(Router);
  private userService: UserService = inject(UserService);

  logOut() {
    this.userService.clearData();
    this.router.navigate(['/login']);
  }
}
