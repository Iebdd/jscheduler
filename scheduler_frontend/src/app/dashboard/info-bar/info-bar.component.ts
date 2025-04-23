import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { Booking, Course, Info, Room, Segment, User } from '../../model/interfaces';
import { InfoStates } from '../../model/enums';
import { LoadDataService } from '../../Services/load-data.service';
import { CourseNamePipe } from '../../Pipes/course-name.pipe';
import { RoomNamePipe } from '../../Pipes/room-name.pipe';
import { DatePipe } from '@angular/common';
import { LocalService } from '../../Services/local.service';

@Component({
  selector: 'app-info-bar',
  imports: [CourseNamePipe, RoomNamePipe, DatePipe],
  templateUrl: './info-bar.component.html',
  styleUrl: './info-bar.component.scss'
})
export class InfoBarComponent implements OnInit {

  private _userService: UserService = inject(UserService);
  private _localService: LocalService = inject(LocalService);
  protected States = InfoStates;
  protected delete_pressed: boolean = false;

  protected _courses: Course[] = [];
  protected _valid_courses: Course[] = [];
  protected _rooms: Room[] = [];
  protected _bookings: string = '';
  protected _info: Info = {
    booking_id: '',
    room: -1,
    info: false,
    start: '',
    end: '',
    index: -1
  }
  protected _permission: number = 0;

  protected _info_booking: Booking = {
    bookings_id : '',
    room_id: '',
    course_id: '',
    start: new Date(),
    end: new Date(),
    status: ''
  }
  protected _date: Date = new Date();
  protected _timelines: Segment[][] = [];
  protected _end_timeline: Segment[] = [];
  protected _hour_markers: string[] = [];
  protected _info_state: InfoStates = InfoStates.Default;

  addItem() {
    this._info_state = InfoStates.NewCourseRoom;
  }

  setRoom(room_no: number) {
    this._info.room = room_no;
    this._info_state = InfoStates.NewCourseStart;
  }

  setStart(index: number) {
    this._info.start = this._hour_markers[index];
    this.setEndTimeline(index);
    this._info_state = InfoStates.NewCourseEnd;
  }

  setEnd(index: number) {
    this._info.end = this._hour_markers[index];
    this._info_state = InfoStates.NewCourseCourse;
  }

  setCourse(course_no: number) {
    var start_time = this._date.toISOString().split('T')[0] + "T" + this._info.start;
    var end_time = this._date.toISOString().split('T')[0] + "T" + this._info.end;
    this._userService.addBooking(this._courses[course_no].id, this._rooms[this._info.room].id, start_time, end_time);
    this._info_state = InfoStates.Default;
  }

  setEndTimeline(start_index: number): void {
    this._end_timeline = [];
    var cur_timeline: Segment[] = this._timelines[this._info.room];
    for(let index = 0; index < cur_timeline.length; index++) {
      if(index < start_index) {
        this._end_timeline.push({"booking_id": "", "status": -2, "empty": true});
      } else if(cur_timeline[index].booking_id == null) {
        this._end_timeline.push(cur_timeline[index])
      } else {
        break;
      }
    }
  }

  deleteBooking(): void {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._userService.removeBooking(this._info_booking.bookings_id, token);
      this._userService.clearInfo();
      this._userService.setInfoState(InfoStates.Default);
      this.delete_pressed = false;
    }

  }

  enrollStudent() {
    console.log(this._permission);
  }

  assignBooking(): void {
    var bookings: Booking[] = JSON.parse(this._bookings);
    for(var booking of bookings) {
      if(booking.bookings_id == this._info.booking_id) {
        this._info_booking = booking;
        this._info_state = InfoStates.CourseInfo;
        return;
      }
    }
  }

  getInfo(): void {
    this._userService.getInfo()
      .subscribe(info => this._info = info)
  }

  getCourses(): void {
    this._userService.getCourses()
      .subscribe(courses => this._courses = courses);
  }

  getRooms(): void {
    this._userService.getRooms()
      .subscribe(rooms => this._rooms = rooms);
  }

  getTimelines(): void {
    this._userService.getTimelines()
      .subscribe(timelines => this._timelines = timelines);
  }

  getHours(): void {
    this._hour_markers = this._userService.getHours();
  }

  getDate(): void {
    this._userService.getDate()
      .subscribe(date => this._date = date);
  }


  newCourseWithValues() {
    this._info.start = this._hour_markers[this._info.index];
    this.setEndTimeline(this._info.index);
  }

  getInfoState(): void {
    this._userService.getInfoState()
      .subscribe(infostate => {
        if(infostate == InfoStates.NewCourseEnd) {
          this.newCourseWithValues();
          this._info_state = infostate;
        } else if (infostate == InfoStates.CourseInfo) {
          this.assignBooking();
        } else {
          this._info_state = infostate
        }
      });
  }

  getBookings(): void {
    this._userService.getBookings()
      .subscribe(bookings => this._bookings = bookings);
  }

  getPermissions(): void {
    this._userService.getPermission()
      .subscribe(permission => this._permission = permission)
  }

  ngOnInit(): void {
    this.getRooms();
    this.getCourses();
    this.getInfo();
    this.getInfoState();
    this.getTimelines();
    this.getHours();
    this.getDate();
    this.getBookings();
    this.getPermissions();
  }
}
