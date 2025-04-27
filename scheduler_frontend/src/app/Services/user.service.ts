import { inject, Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Booking, Course, Info, Inscription, Room, Segment, User, UserBooking, UserToken } from '../model/interfaces';
import { LoadDataService } from './load-data.service';
import { LocalService } from './local.service';
import { Router } from '@angular/router';
import { StatusService } from './status.service';
import { InfoStates } from '../model/enums';
import { CourseNamePipe } from '../Pipes/course-name.pipe';

/**
 * Provides data to and from the user
 */
@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor() {}

  private _loadDataService: LoadDataService = inject(LoadDataService);
  private _localService: LocalService = inject(LocalService);
  private _router: Router =  inject(Router);
  private _statusService: StatusService = inject(StatusService);

  private _userData = new BehaviorSubject<User>(
    {
      user_id: '',
      role: 0,
      firstName: '',
      lastName: '',
      email: ''
    });

  protected _hour_markers: string[] = [
    "07:00", "07:15", "07:30", "07:45",
    "08:00","08:15", "08:30", "08:45", 
    "09:00","09:15", "09:30", "08:45", 
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
  

  private _courses = new BehaviorSubject<Course[]>([]);
  private _permission = new BehaviorSubject<number>(0);
  private _rooms = new BehaviorSubject<Room[]>([]);
  private _timelines = new BehaviorSubject<Segment[][]>([]);
  private _date = new BehaviorSubject<Date>(new Date());
  private _temp_time: string[][] = [];
  private _bookings = new BehaviorSubject<string>("");
  private _info = new BehaviorSubject<Info>({
    booking_id: '',
    room: -1,
    info: false,
    start: '',
    end: '',
    index: -1
  })
  private _users = new BehaviorSubject<User[]>([]);
  private _inscriptions = new BehaviorSubject<Inscription[]>([]);
  private _info_state = new BehaviorSubject<InfoStates>(InfoStates.Default);

  getUserData(): Observable<User> {
    return this._userData.asObservable();
  }

  getCourses(): Observable<Course[]> {
    return this._courses.asObservable();
  }

  getRooms(): Observable<Room[]> {
    return this._rooms.asObservable();
  }

  getBookings(): Observable<string> {
    return this._bookings.asObservable();
  }

  getDate(): Observable<Date> {
    return this._date.asObservable();
  }

  getTimelines(): Observable<Segment[][]> {
    return this._timelines.asObservable();
  }

  getInfo(): Observable<Info> {
    return this._info.asObservable();
  }

  getInfoState(): Observable<InfoStates> {
    return this._info_state.asObservable();
  }

  getHours(): string[] {
    return this._hour_markers;
  }

  getStatus(booking_id: string): number {
    var obj_bookings: Booking[] = JSON.parse(this._bookings.value);
    for (var booking of obj_bookings) {
      if (booking.bookings_id == booking_id) {
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

  convertStatus(status: string): number {
    switch(status) {
      case 'Set':
        return 0;
      case 'Planned':
        return 1;
      case 'Preference':
        return 2;
      default:
        return -1;
    }
  }

  getUsers(): Observable<User[]> {
    return this._users.asObservable();
  }

  getPermission(): Observable<number> {
    return this._permission.asObservable();
  }

  getInscriptions(): Observable<Inscription[]> {
    return this._inscriptions.asObservable();
  }

  setUserData(user: User): void {
    this._userData.next(user);
    this._permission.next(user.role);
  }

  setCourses(courses: Course[]) {
    this._courses.next(courses);
  }

  setRooms(rooms: Room[]): void {
    this._rooms.next(rooms);
  }

  setBookings(bookings: string): void {
    this._bookings.next(bookings);
  }

  setDate(date: Date): void {
    this._date.next(date);
  }

  setTimelines(timelines: Segment[][]): void {
    this._timelines.next(timelines);
  }

  setInfo(booking_id: string, room: number, start: string, end: string, info: boolean, index: number): void {
    this._info.next({"booking_id": booking_id, "room": room, "start": start, "end": end, "info": info, "index": index});
  }

  setInfoState(state: InfoStates): void {
    this._info_state.next(state);
  }

  setUsers(users: User[]): void {
    this._users.next(users);
  }

  setInscriptions(inscriptions: Inscription[]): void {
    this._inscriptions.next(inscriptions);
  }

  addBooking(course_id: string, room_id: string, start: string, end: string) {
    var token: string | null = this._localService.getItem("user_token");
    var preference: boolean = (this._userData.value.role < 2);
    if(token != null) {
      this._loadDataService.addBooking(course_id, room_id, start, end, token, preference)
        .subscribe({
          next: (value) => {
            if(token != null) { //Type guards do not extend into anonymous functions
              this.requestNewData(token);
            }
          }
        })
    }
  }

  addInscription(user_id: string, course_id: string): void {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._loadDataService.addInscription(user_id, course_id, token)
        .subscribe({
          next: (value) => {
            if(this._userData.value.role < 1 && token != null) {
              this.requestNewData(token);
            }
          },
          error: (error) => {
            console.log(error);
            this._statusService.setDashboardStatus("This user is already enrolled in this course");
          }
        })
    }
  }

  addCourse(course_name: string) {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._loadDataService.addCourse(course_name, token)
        .subscribe({
          next: (value) => {
            if(token != null) {
              this.requestNewData(token);
            }
        }})
    }
  }

  addRoom(room_name: string) {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._loadDataService.addRoom(room_name, token)
        .subscribe({
          next: (value) => {
            if(token != null) {
              this.requestNewData(token);
            }
        }})
    }
  }

  removeInscription(user_id: string, course_id: string): void {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._loadDataService.removeInscription(user_id, course_id, token)
        .subscribe({
          next: (value) => {
            if(this._userData.value.role < 1 && token != null) {
              this.requestNewData(token);
            }
          },
          error: (error) => {
            console.log(error);
            this._statusService.setDashboardStatus("This user is not enrolled in this course");
          }
        })
    }
  }

  removeBooking(booking_id: string, token: string): void {
    this._loadDataService.removeBooking(booking_id, token)
      .subscribe();
      this.requestNewData(token);
  }

  updateBooking(new_status: string, booking_id: string) {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._loadDataService.updateBookingStatus(new_status, booking_id, token)
        .subscribe(() => {
          if(token != null) {
            this.requestNewData(token);
          }
        });
    }
  }

  /////

  updateFirstName(first_name: string, user_id: string) {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._loadDataService.updateFirstName(first_name, user_id, token)
        .subscribe(() => {
          if(token != null) {
            this.requestNewData(token);
          }
        });
    }
  }

  updateLastName(last_name: string, user_id: string) {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._loadDataService.updateLastName(last_name, user_id, token)
        .subscribe(() => {
          if(token != null) {
            this.requestNewData(token);
          }
        });
    }
  }
 
  updateEmail(email: string, user_id: string) {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._loadDataService.updateEmail(email, user_id, token)
        .subscribe(() => {
          if(token != null) {
            this.requestNewData(token);
          }
        });
    }
  }

  updateRole(role: number, user_id: string) {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._loadDataService.updateRole(role, user_id, token)
        .subscribe(() => {
          if(token != null) {
            this.requestNewData(token);
          }
        });
    }
  }

  updatePassword(password: string, user_id: string) {
    var token: string | null = this._localService.getItem("user_token");
    if(token != null) {
      this._loadDataService.updatePasswordViaToken(password, user_id, token)
        .subscribe(() => {
          if(token != null) {
            this.requestNewData(token);
          }
        });
    }
  }

  ////

  mergeTimelines() {
    var filled: boolean = true;
    for(var index = 0; index < this._rooms.value.length; index++) {
      if(this._temp_time[index].length == 0) {  //Only push the buffer if all slots are filled
        filled = false;
      }
    }
    if (filled == true) {
      this._timelines.next(this.constructTables(this._temp_time));
    }
  }

  setUpTimelineRequest(date: string, token: string): void {
    this._temp_time = [];
    for(var index = 0; index < this._rooms.value.length; index++) {
      this._temp_time.push([]);     // Make sure there are as many timeline slots as there are rooms
    }
    for(const room of this._rooms.value) {
      this.requestTimeline(room.id, date, token);
    }
  }

  clearInfo(): void {
    this._info.next({"booking_id": '', "room": -1, "start": '', "end": '', "info": false, "index": -1});
  }

  constructTables(timeline: string[][]): Segment[][] {
    var temp_segm: Segment[][] = [];
    for(let outer_index = 0; outer_index < timeline.length; outer_index++) {
      temp_segm.push([]);
      for(let inner_index = 0; inner_index < timeline[outer_index].length; inner_index++) {
        var new_segment: Segment = {
              booking_id: timeline[outer_index][inner_index], 
              empty: (timeline[outer_index][inner_index] == null) ? true : false, 
              status: this.getStatus(timeline[outer_index][inner_index])};
        temp_segm[outer_index].push(new_segment);
      }
    }
    return temp_segm;
  }

  clearData() {
    var blank_user: User = {
      user_id: '',
      role: 0,
      firstName: '',
      lastName: '',
      email: ''
    }
    this.setUserData(blank_user);
    this.setCourses([]);
    this.setRooms([]);
    this.setBookings("{}");
    this.setUsers([]);
    this._temp_time = [];
    this.mergeTimelines();
  }

  requestNewData(token: string) {
    var user: User = this._userData.value;
    this.clearData();
    this.requestBaseData(user, token, this._date.value);
  }

  requestTimeline(room_id: string, date: string, token: string) {
    this._loadDataService.getTimeline(room_id, date, token)
      .subscribe({
        next: (value) => {
          if(value.body != null) {
            for(var index = 0; index < this._rooms.value.length; index++) {
              if(this._rooms.value[index].id == room_id) {  //Assign the timeline to the correct room index
                this._temp_time[index] = JSON.parse(JSON.stringify(value.body));
              }
            }
            this.mergeTimelines();
          }
        }
      })
  }

  requestCourses(): void {
    this._loadDataService.getCourses()
    .subscribe({
      next: (value) => {
        if(value.body != null) {
          this._statusService.setLoadingStatus("Done");
          this.setCourses(JSON.parse(JSON.stringify(value.body)));
        }
      }
    });
  }

  requestRooms(user_token: string, date: Date): void {
    this._loadDataService.getRooms()
    .subscribe({
      next: (value) => {
        if(value.body != null) {
            this._statusService.setLoadingStatus("Done");
            this.setRooms(JSON.parse(JSON.stringify(value.body)));
            this.setUpTimelineRequest(new Date(date).toISOString().split('T')[0], user_token);

        }
      }
    })
  }

  requestUserBookings(user_id: string, user_token: string) {
    this._loadDataService.getUserBookings(user_id, user_token)
      .subscribe({
        next: (value) => {
          if(value.body != null) {
            this._statusService.setLoadingStatus("Done");
            this.setBookings(JSON.stringify(value.body));
            this._router.navigate(['/dashboard']);
          }
        }
      })
  }

  requestBookings(user_token: string) {
    this._loadDataService.getAllBookings(user_token)
      .subscribe({
        next: (value) => {
          if(value.body != null) {
            this._statusService.setLoadingStatus("Done");
            this.setBookings(JSON.stringify(value.body));
            this._statusService.setLoadingStatus("Data loaded. Redirecting to dashboard");
            this._router.navigate(['/dashboard']);
          }
        }
      })
  }

  requestUsers(user_token: string) {
    this._loadDataService.getUsers(user_token)
      .subscribe({
        next: (value) => {
          if(value.body != null) {
            this.setUsers(JSON.parse(JSON.stringify(value.body)));
          }
        }
      })
  }

  requestInscriptions(user_token: string) {
    this._loadDataService.getInscriptions(user_token)
      .subscribe({
        next: (value) => {
          if(value.body != null) {
            this.setInscriptions(JSON.parse(JSON.stringify(value.body)));
          }
        }
      })
  }


  requestBaseData(user: User, user_token: string, date: Date): void {
    this._statusService.setLoadingStatus(`Welcome, ${user.firstName}!`);
    this.setUserData(user);
    this._statusService.setLoadingStatus("Requesting course data ...");
    this.requestCourses();
    this._statusService.setLoadingStatus("Requesting room data ...");
    this.requestRooms(user_token, date);
    this._statusService.setLoadingStatus("Requesting booking data ...");
    this.startAssistantRequest(user, user_token);
  }

  startAssistantRequest(user: User, user_token: string) {
    if (user.role == 0) {
      console.log(user);
      this.requestUserBookings(user.user_id, user_token);
    } else {
      this.requestUsers(user_token);
      this.requestBookings(user_token);
      this.requestInscriptions(user_token);
    }
  }

  init(): void {
    this._router.navigate(['/loading']);
    var login: string | null = this._localService.getItem("auto_login");
    if(login != null && login == "true") {
      this._statusService.setLoadingStatus("Getting active user ...");
      var user_token: string | null = this._localService.getItem("user_token");
      if(user_token != null) {
        this._statusService.setLoadingStatus("Found. Verifying ...");
        this.initFromToken(user_token);
      } else {
        this._statusService.setLoadingStatus("None found \n Redirecting to login");
        this._router.navigate(['/login']);
      }
    } else {
      this._router.navigate(['/login']);
    }
  }

  initFromToken(user_token: string): void {
      this._loadDataService.verifyToken(user_token)
        .subscribe({
          next: (value) => {
            if(value.ok && value.body != null) {
              this._statusService.setLoadingStatus("Done. Loading data ...");
              console.log(value.body);
              this.requestBaseData(JSON.parse(JSON.stringify(value.body)), user_token, new Date());
            } else {
              this._statusService.setLoadingStatus("Invalid token \n Redirecting to login");
              this._localService.deleteItem("user_token");
              this._router.navigate(['/login']);
            }
          },
          error: (value) => {
            this._statusService.setLoadingStatus("Invalid token \n Redirecting to login");
            this._localService.deleteItem("user_token");
            this._router.navigate(['/login']);
          }
        })
    }

  initWithoutToken(email: string, password: string, auto: boolean) {
    this._loadDataService.addToken(email, password)
      .subscribe({
        next: (value) => {
          if (value != null) {
            var token: UserToken = JSON.parse(JSON.stringify(value.body));
            if(token.tokens != null) {
              if(auto) {
                this._localService.setItem("auto_login", "true");
              } else {
                this._localService.setItem("auto_login", "false");
              }
              this._localService.setItem("user_token", token.tokens[0]);
              this.initFromToken(token.tokens[0]);
            }
          }
        },
        error: () => {
          this._statusService.setLoginStatus("Something has gone wrong. Please try again at a later point");
          this._router.navigate(['/login']);
        }
      })
  }

    checkTokens(tokens: string[]): string {
      var local_token: string | null = this._localService.getItem("user_token");
      if(local_token != null) {
        for(let index = 0; index < tokens.length; index++) {
          if(tokens[index] == local_token) {
            return tokens[index];
          }
        }
      }
      return "";
    }
}
