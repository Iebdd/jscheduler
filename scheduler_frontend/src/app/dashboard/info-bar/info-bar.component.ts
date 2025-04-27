import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { Booking, Course, Info, Inscription, Room, Segment, User } from '../../model/interfaces';
import { InfoStates } from '../../model/enums';
import { CourseNamePipe } from '../../Pipes/course-name.pipe';
import { RoomNamePipe } from '../../Pipes/room-name.pipe';
import { DatePipe } from '@angular/common';
import { LocalService } from '../../Services/local.service';
import { StatusService } from '../../Services/status.service';
import { FormsModule } from '@angular/forms';

/**
 * Provides the info bar, the right hand side of the dashboard
 */
@Component({
  selector: 'app-info-bar',
  imports: [CourseNamePipe, RoomNamePipe, DatePipe, FormsModule],
  templateUrl: './info-bar.component.html',
  styleUrl: './info-bar.component.scss'
})
export class InfoBarComponent implements OnInit {

  private _userService: UserService = inject(UserService);
  private _localService: LocalService = inject(LocalService);
  private _statusService: StatusService = inject(StatusService);
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

  protected _user_detail = {
    user_id: '',
    role: -1,
    firstName: '',
    lastName: '',
    email: ''
  }
  protected _input_field: string = '';
  protected _active_user: User = {
    user_id: '',
    role: -1,
    firstName: '',
    lastName: '',
    email: ''
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
  protected _dashboard_status: string = '';
  protected _users: User[] = [];
  protected _inscriptions: Inscription[] = [];

  addBooking() {
    this._info_state = InfoStates.NewCourseRoom;
  }

  addCourse() {
    for(var course of this._courses) {
      if(this._input_field == course.courseName) {
        this._statusService.setDashboardStatus("A course with that name already exists.");
        return;
      }
    }
    this._userService.addCourse(this._input_field);
    this.clearInput(InfoStates.Default);
  }

  addRoom() {
    for(var room of this._rooms) {
      if(this._input_field == room.roomName) {
        this._statusService.setDashboardStatus("A room with that name already exists.")
        return;
      }
    }
    this._userService.addRoom(this._input_field);
    this.clearInput(InfoStates.Default);
  }

  newMenu() {
    this._statusService.setDashboardStatus('');
    this._info_state = InfoStates.Menu;
  }

  userDetail(user: User) {
    this._user_detail = user;
    this._info_state = InfoStates.ManagementDetail;
  }

  updateFirstName() {
    if(this._input_field != '') {
      this._userService.updateFirstName(this._input_field, this._user_detail.user_id);
      this._input_field = '';
      this._info_state = InfoStates.Management;
    } else {
      this._statusService.setDashboardStatus("Input cannot be empty.")
    }
  }

  updateLastName() {
    if(this._input_field != '') {
      this._userService.updateLastName(this._input_field, this._user_detail.user_id);
      this._input_field = '';
      this._info_state = InfoStates.Management;
    } else {
      this._statusService.setDashboardStatus("Input cannot be empty.")
    }
  }

  updateEmail() {
    if(this._input_field != '') {
      this._userService.updateEmail(this._input_field, this._user_detail.user_id);
      this._input_field = '';
      this._info_state = InfoStates.Management;
    } else {
      this._statusService.setDashboardStatus("Input cannot be empty.")
    }
  }

  updateRole(role: number) {
    this._userService.updateRole(role, this._user_detail.user_id);
    this._info_state = InfoStates.Management;
  }

  updatePassword() {
    if(this._input_field != '') {
      this._userService.updatePassword(this._input_field, this._user_detail.user_id);
      this._input_field = '';
      this._info_state = InfoStates.Management;
    } else {
      this._statusService.setDashboardStatus("Input cannot be empty.")
    }
  }

  validEmail(email: string): boolean {
    if(email.match(/(?!(^[.-].*|[^@]*\.@|.*\.{2,}.*)|^.{254}.)([a-zA-Z0-9!#$%&'*+\/=?^_`{|}~.-]+@)(?!-.*|.*-\.)([a-zA-Z0-9-]{1,63}\.)+[a-zA-Z]{2,15}/g)) {
      return true;
    } else {                   //  Email regex pattern taken from this StackOverflow answer: 
      return false;            //  https://stackoverflow.com/questions/27000681/how-to-verify-form-input-using-html5-input-verification/27000682#27000682
    }
  }

  clearDetail() {
    this._user_detail = {
      user_id: '',
      role: -1,
      firstName: '',
      lastName: '',
      email: ''
    }
    this._info_state = InfoStates.Management;
  }

  clearInput(next_state: InfoStates) {
    this._info_state =  next_state;
    this._input_field = '';
    this._info_booking = {
      bookings_id: '',
      course_id: '',
      room_id: '',
      start: new Date(),
      end: new Date(),
      status: ''
    };
    this._statusService.setDashboardStatus('');
  }

  confirmPreference(booking_id: string) {
    this._userService.updateBooking('Set', booking_id);
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

  setBooking(course_no: number) {
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

  setEnrolmentCourse(course_id: string): void {
    this._info_booking.course_id = course_id;
    if(this._active_user.role > 0) {
      this._info_state=InfoStates.EnrollStudent;
    } else {
      this.enrollStudent(this._active_user.user_id);
    }
  }

  enrollStudent(user_id: string) {
    if(user_id == '') {
      if(this._active_user.role < 1) {
        this._userService.addInscription(this._active_user.user_id, this._info_booking.course_id);
        this._info_state = InfoStates.Default;
    } else {
        this._info_state = InfoStates.EnrollStudent;
      }
    } else {
      this._userService.addInscription(user_id, this._info_booking.course_id);
      this._info_state = InfoStates.Default;
    }
  }

  unenrollStudent(user_id: string) {
    if(user_id == '') {
      if(this._active_user.role < 1) {
        this._userService.removeInscription(this._active_user.user_id, this._info_booking.course_id);
        this._info_state = InfoStates.Default;
      } else {
        this._info_state = InfoStates.UnenrollStudent;
      }
    } else {
      this._userService.removeInscription(user_id, this._info_booking.course_id);
      this._info_state = InfoStates.Default;
    }
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

  getDashboardInfo(): void {
    this._statusService.getDashboardStatus()
      .subscribe(status => this._dashboard_status = status);
  }

  getUsers(): void {
    this._userService.getUsers()
      .subscribe(users => this._users = users);
  }

  getActiveUser(): void {
    this._userService.getUserData()
      .subscribe(userData => this._active_user = userData);
  }

  getInscriptions(): void {
    this._userService.getInscriptions()
      .subscribe(inscriptions => this._inscriptions = inscriptions);
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
    this.getUsers();
    this.getActiveUser();
    this.getInscriptions();
    this.getDashboardInfo();
  }
}
