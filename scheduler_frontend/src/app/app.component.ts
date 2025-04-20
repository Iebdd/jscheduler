import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { LoadDataService } from './Services/load-data.service';
import { User } from './model/interfaces';
import { LocalService } from './Services/local.service';
import { UserService } from './Services/user.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'Scheduler';

  constructor (){}

  private userService: UserService = inject(UserService);
  

  ngOnInit(): void {
    this.userService.init();
  }

}
