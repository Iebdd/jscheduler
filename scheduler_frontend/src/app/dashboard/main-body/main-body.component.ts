import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { Booking, Course, Mouseover, Room, Segment, User } from '../../model/interfaces';
import { CommonModule, DatePipe } from '@angular/common';
import { HostListener } from "@angular/core";
import { LocalService } from '../../Services/local.service';
import { Router } from '@angular/router';
import { StatusService } from '../../Services/status.service';
import { CourseNamePipe } from '../../Pipes/course-name.pipe';
import { InfoStates } from '../../model/enums';

/**
 * Provides the main body, the left hand side of the dashboard
 */
@Component({
  selector: 'app-main-body',
  imports: [DatePipe, CommonModule, CourseNamePipe],
  templateUrl: './main-body.component.html',
  styleUrl: './main-body.component.scss'
})
export class MainBodyComponent implements OnInit {

  constructor() {
    this.onResize();
    this._date = new Date();
  }

  private _userService: UserService = inject(UserService);
  private _localService: LocalService = inject(LocalService);
  private _router: Router = inject(Router);
  private _statusService: StatusService = inject(StatusService);

  @HostListener('window:resize', ['$event'])
  onResize(event?: any) {
   this._screenHeight = window.innerHeight;
   this._screenWidth = window.innerWidth;
}

protected _hour_markers: string[] = [];

  protected _active_user: User = {
    user_id: '',
    role: 0,
    firstName: '',
    lastName: '',
    email: ''
  }

  protected _courses: Course[] = [];
  protected _rooms: Room[] = [];
  protected _date: Date;
  protected _bookings: string = "";       //Bookings are saved as a string because the object liked losing its id
  protected _timelines: Segment[][] = [];
  protected _screenHeight = 0;
  protected _screenWidth = 0;
  protected _mouseover: Mouseover = {
    booking_id: '',
    room: -1,
    index: -1
  }


  protected _sideBorder = {'border-top': "0", 'border-left': "2px black solid", 'border-right': "2px black solid", 'border-bottom': "0"};
  protected _topBorder = {'border-top': "2px black solid", 'border-left': "2px black solid", 'border-right': "2px black solid", 'border-bottom': "0"};
  protected _botBorder = {'border-top': "0", 'border-left': "2px black solid", 'border-right': "2px black solid", 'border-bottom': "2px black solid"};

  previousDay() {
    this._timelines = [];
    var next_date: Date = new Date(this._date.setDate(this._date.getDate() - 1));
    this.getNewTimeline(next_date);
  }

  nextDay() {
    this._timelines = [];
    this._userService.setInfoState(InfoStates.Default);
    this._userService.clearInfo();
    var next_date: Date = new Date(this._date.setDate(this._date.getDate() + 1));
    this.getNewTimeline(next_date);
  }

  setHover(booking_id: string, room: number, index: number) {
    this._mouseover = {"booking_id": booking_id, "room": room, "index": index};
  }

  newCourse(room: number, index: number) {
    if(this._active_user.role != 0) {
      this._userService.setInfo('', room, '', '', false, index);
      this._userService.setInfoState(InfoStates.NewCourseEnd);
    }
  }

  bookingInfo(booking_id: string, room: number) {
    var bookings: Booking[] = JSON.parse(this._bookings);
    for(var booking of bookings) {
      if(booking.bookings_id == booking_id) {
        this._userService.setInfo(booking_id, room, new Date(booking.start).toISOString(),new Date(booking.end).toISOString(), true, -1);
        this._userService.setInfoState(InfoStates.CourseInfo);
        return;
      }
    }
  }

  getNewTimeline(next_date: Date) {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._userService.setDate(next_date);
      this._userService.setTimelines([]);
      this._userService.setUpTimelineRequest(next_date.toISOString().split("T")[0], token);
    } else {
      this._statusService.setLoginStatus("Session expired. Please log in again");
      this._router.navigate(['/login']);
    }
  }

  setActiveUser(user: User): void {
    this._active_user.role = user.role;
    this._active_user.firstName = user.firstName;
    this._active_user.lastName = user.lastName;
    this._active_user.email = user.email;
  }

  getUserData(): void {
    this._userService.getUserData()
      .subscribe(userData => this._active_user = userData);
  }

  getCourses(): void {
    this._userService.getCourses()
      .subscribe(courses => this._courses = courses);
  }

  getRooms(): void {
    this._userService.getRooms()
      .subscribe(rooms => this._rooms = rooms);
  }

  getBookings(): void {
    this._userService.getBookings()
      .subscribe(bookings => {
        this._bookings = bookings
      });
  }
  
  getDate(): void {
    this._userService.getDate()
      .subscribe(date => this._date = date);
  }

  getTimeline(): void {
    this._userService.getTimelines()
      .subscribe(timeline => this._timelines = timeline);
  }

  getHours(): void {
    this._hour_markers = this._userService.getHours()
  }

  ngOnInit(): void {
    this.getUserData();
    this.getCourses();
    this.getRooms();
    this.getBookings();
    this.getDate();
    this.getTimeline();
    this.getHours();
  }
}
