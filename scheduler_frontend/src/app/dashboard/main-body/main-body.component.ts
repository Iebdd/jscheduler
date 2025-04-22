import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { Booking, Course, Mouseover, Room, Segment, User } from '../../model/interfaces';
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
  protected _bookings: string = "";       //Bookings are saved as a string because the object liked losing its id
  protected _timelines: Segment[][] = [];
  protected _screenHeight = 0;
  protected _screenWidth = 0;
  protected _mouseover: Mouseover = {
    booking_id: '',
    room: -1,
    index: -1
  }
  protected _hour_markers: string[] = [
    "7:00", "7:15", "7:30", "7:45",
    "8:00","8:15", "8:30", "8:45", 
    "9:00","9:15", "9:30", "8:45", 
    "10:00","10:15", "10:30", "10:45", 
    "11:00","11:15", "11:30", "11:45",  
    "12:00","12:15", "12:30", "12:45",  
    "13:00","13:15", "13:30", "13:45",  
    "14:00","14:15", "14:30", "14:45",  
    "15:00","15:15", "15:30", "15:45",  
    "16:00","16:15", "16:30", "16:45",  
    "17:00","17:15", "17:30", "17:45",  
    "18:00","18:15", "18:30", "18:45",  
    "19:00","19:15", "19:30", "19:45",  
    "20:00"];

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

  setHover(booking_id: string, room: number, index: number) {
    console.log(`ID: ${booking_id} - room: ${room} - index: ${index}`);
    this._mouseover = {"booking_id": booking_id, "room": room, "index": index};
  }

  newCourse(index: number, time: string) {
    console.log()
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
  }

  getStatus(bookings_id: string): number {
    var obj_bookings: Booking[] = JSON.parse(this._bookings);
    for (var booking of obj_bookings) {
      if (booking.bookings_id == bookings_id) {
        switch(booking.status) {
          case "Set":
            return 0;
          case "Planned":
            return 1;
          case "Preference":
            return 2;
          default:
            return -1;
        }
      }
    }
    return -1;
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
