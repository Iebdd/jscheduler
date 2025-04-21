import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { Booking, Course, Room, Segment, User } from '../../model/interfaces';
import { CommonModule, DatePipe } from '@angular/common';
import { HostListener } from "@angular/core";
import { LocalService } from '../../Services/local.service';
import { Router } from '@angular/router';
import { StatusService } from '../../Services/status.service';
import { CourseNamePipe } from '../../Pipes/course-name.pipe';

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

  protected _active_user: User = {
    userId: '',
    role: 0,
    firstName: '',
    lastName: '',
    email: ''
  }

  protected _courses: Course[] = [];
  protected _rooms: Room[] = [];
  protected _date: Date;
  protected _bookings: Booking[] = [];
  protected _timelines: Segment[][] = [];
  protected _screenHeight = 0;
  protected _screenWidth = 0;
  protected _hour_markers: string[] = [
    "8:00","", "", "", 
    "9:00","", "", "", 
    "10:00","", "", "", 
    "11:00","", "", "",  
    "12:00","", "", "",  
    "13:00","", "", "",  
    "14:00","", "", "",  
    "15:00","", "", "",  
    "16:00","", "", "",  
    "17:00","", "", "",  
    "18:00","", "", "",  
    "19:00","", "", "",  
    "20:00","", "", "", ];

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
    var next_date: Date = new Date(this._date.setDate(this._date.getDate() + 1));
    this.getNewTimeline(next_date);
  }

  newCourse(index: number) {
    
  }

  courseInfo(course_id: string) {
    console.log(course_id);
  }

  constructTables(timeline: string[][]) {
    for(let outer_index = 0; outer_index < timeline.length; outer_index++) {
      this._timelines.push([]);
      for(let inner_index = 0; inner_index < timeline[outer_index].length; inner_index++) {
        var new_segment: Segment = {
              booking_id: timeline[outer_index][inner_index], 
              empty: (timeline[outer_index][inner_index] == null) ? true : false, 
              status: this.getStatus(timeline[outer_index][inner_index])};
        this._timelines[outer_index].push(new_segment);
      }
    }
    console.log(this._timelines);
  }

  getStatus(booking_id: string): number {
    for (var booking of this._bookings) {
      if (booking.id = booking_id) {
        switch(booking.status) {
          case "Set":
            return 0;
          case "Planned":
            return 1;
          case "Preference":
            return 2;
          default:
            return 0;
        }
      }
    }
    return 0;
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
      .subscribe(bookings => this._bookings = bookings);
  }
  
  getDate(): void {
    this._userService.getDate()
      .subscribe(date => this._date = date);
  }

  getTimeline(): void {
    this._userService.getTimelines()
      .subscribe(timeline => this.constructTables(timeline));
  }

  ngOnInit(): void {
    this.getUserData();
    this.getCourses();
    this.getRooms();
    this.getBookings();
    this.getDate();
    this.getTimeline();
  }
}
