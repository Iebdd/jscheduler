import { Component, inject, OnInit } from '@angular/core';
import { Booking, Course, Room, User } from '../model/interfaces';
import { UserService } from '../Services/user.service';
import { Subject, takeUntil } from 'rxjs';
import { InfoBarComponent } from "./info-bar/info-bar.component";
import { MainBodyComponent } from "./main-body/main-body.component";
import { TopBarComponent } from "./top-bar/top-bar.component";

@Component({
  selector: 'app-dashboard',
  imports: [InfoBarComponent, MainBodyComponent, TopBarComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  private _userService: UserService = inject(UserService);
  private _destroyed = new Subject<void>;

  protected _active_user: User = {
    userId: '',
    role: 0,
    firstName: '',
    lastName: '',
    email: ''
  }

  protected _courses: Course[] = [];
  protected _rooms: Room[] = [];
  protected _bookings: Booking[] = [];


  setActiveUser(user: User): void {
    this._active_user.role = user.role;
    this._active_user.firstName = user.firstName;
    this._active_user.lastName = user.lastName;
    this._active_user.email = user.email;
  }

  getUserData(): void {
    this._userService.getUserData()
      .pipe(takeUntil(this._destroyed))
      .subscribe(userData => this._active_user = userData);
  }

  getCourses(): void {
    this._userService.getCourses()
      .pipe(takeUntil(this._destroyed))
      .subscribe(courses => this._courses = courses);
  }

  getRooms(): void {
    this._userService.getRooms()
      .pipe(takeUntil(this._destroyed))
      .subscribe(rooms => this._rooms = rooms);
  }

  getBookings(): void {
    this._userService.getBookings()
      .pipe(takeUntil(this._destroyed))
      .subscribe(bookings => this._bookings = bookings);
  }

  ngOnInit(): void {
    this.getUserData();
    this.getCourses();
    this.getRooms();
    this.getBookings();
  }
}
