import { inject, Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Booking, Course, Room, User, UserToken } from '../model/interfaces';
import { LoadDataService } from './load-data.service';
import { LocalService } from './local.service';
import { Parse } from '../model/parse';
import { Router } from '@angular/router';
import { StatusService } from './status.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor() {}

  private loadDataService: LoadDataService = inject(LoadDataService);
  private localService: LocalService = inject(LocalService);
  private router: Router =  inject(Router);
  private statusService: StatusService = inject(StatusService);

  private _userData = new BehaviorSubject<User>(
    {
      userId: '',
      role: 0,
      firstName: '',
      lastName: '',
      email: ''
    });
  

  private _courses = new BehaviorSubject<Course[]>([]);
  private _rooms = new BehaviorSubject<Room[]>([]);
  private _timelines = new BehaviorSubject<string[][]>([]);
  private _date = new BehaviorSubject<Date>(new Date());
  private _temp_time: string[][] = [];
  private _bookings = new BehaviorSubject<string>("");
  private _info = new BehaviorSubject<Booking>({
    room_id: '',
    course_id: '',
    start: new Date(),
    end: new Date(),
    status: '',
    bookings_id: ''
  })

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

  getTimelines(): Observable<string[][]> {
    return this._timelines.asObservable();
  }

  setUserData(user: User): void {
    this._userData.next(user);
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

  setTimelines(timelines: string[][]): void {
    this._timelines.next(timelines);
  }

  mergeTimelines() {
    var filled: boolean = true;
    for(var index = 0; index < this._rooms.value.length; index++) {
      if(this._temp_time[index].length == 0) {  //Only push the buffer if all slots are filled
        filled = false;
      }
    }
    if (filled == true) {
      this._timelines.next(this._temp_time);
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

  clearData() {
    var blank_user: User = {
      userId: '',
      role: 0,
      firstName: '',
      lastName: '',
      email: ''
    }
    this.setUserData(blank_user);
    this.setCourses([]);
    this.setRooms([]);
    this.setBookings("{}");
    this._temp_time = [];
    this.mergeTimelines();
  }

  requestTimeline(room_id: string, date: string, token: string) {
    this.loadDataService.getTimeline(room_id, date, token)
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
    this.loadDataService.getCourses()
    .subscribe({
      next: (value) => {
        if(value.body != null) {
          this.statusService.setLoadingStatus("Done");
          this.setCourses(JSON.parse(JSON.stringify(value.body)));
        }
      }
    });
  }

  requestRooms(user_token: string): void {
    this.loadDataService.getRooms()
    .subscribe({
      next: (value) => {
        if(value.body != null) {
            this.statusService.setLoadingStatus("Done");
            this.setRooms(JSON.parse(JSON.stringify(value.body)));
            this.setUpTimelineRequest(new Date().toISOString().split('T')[0], user_token);

        }
      }
    })
  }

  requestUserBookings(user_id: string, user_token: string) {
    this.loadDataService.getUserBookings(user_id, user_token)
      .subscribe({
        next: (value) => {
          if(value.body != null) {
            this.statusService.setLoadingStatus("Done");
            this.setBookings(JSON.parse(value.body));
            this.router.navigate(['/dashboard']);
          }
        }
      })
  }

  requestBookings(user_token: string) {
    this.loadDataService.getAllBookings(user_token)
      .subscribe({
        next: (value) => {
          if(value.body != null) {
            this.statusService.setLoadingStatus("Done");
            this.setBookings(JSON.stringify(value.body));
            this.statusService.setLoadingStatus("Data loaded. Redirecting to dashboard");
            this.router.navigate(['/dashboard']);
          }
        }
      })
  }


  requestBaseData(user: User, user_token: string): void {
    this.statusService.setLoadingStatus(`Welcome, ${user.firstName}!`);
    this.setUserData(user);
    this.statusService.setLoadingStatus("Requesting course data ...");
    this.requestCourses();
    this.statusService.setLoadingStatus("Requesting room data ...");
    this.requestRooms(user_token);
    this.statusService.setLoadingStatus("Requesting booking data ...");
    if (user.role == 0) {
      this.requestUserBookings(user.userId, user_token);
    } else {
      this.requestBookings(user_token);
    }
  }

  init(): void {
    this.router.navigate(['/loading']);
    var login: string | null = this.localService.getItem("auto_login");
    if(login != null && login == "true") {
      this.statusService.setLoadingStatus("Getting active user ...");
      var user_token: string | null = this.localService.getItem("user_token");
      if(user_token != null) {
        this.statusService.setLoadingStatus("Found. Verifying ...");
        this.initFromToken(user_token);
      } else {
        this.statusService.setLoadingStatus("None found \n Redirecting to login");
        this.router.navigate(['/login']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  initFromToken(user_token: string): void {
      this.loadDataService.verifyToken(user_token)
        .subscribe({
          next: (value) => {
            if(value.ok && value.body != null) {
              this.statusService.setLoadingStatus("Done. Loading data ...");
              this.requestBaseData(JSON.parse(JSON.stringify(value.body)), user_token);
            } else {
              this.statusService.setLoadingStatus("Invalid token \n Redirecting to login");
              this.localService.deleteItem("user_token");
              this.router.navigate(['/login']);
            }
          },
          error: (value) => {
            this.statusService.setLoadingStatus("Invalid token \n Redirecting to login");
            this.localService.deleteItem("user_token");
            this.router.navigate(['/login']);
          }
        })
    }

  initWithoutToken(email: string, password: string, auto: boolean) {
    this.loadDataService.addToken(email, password)
      .subscribe({
        next: (value) => {
          if (value != null) {
            var token: UserToken = JSON.parse(JSON.stringify(value.body));
            if(token.tokens != null) {
              if(auto) {
                this.localService.setItem("auto_login", "true");
              } else {
                this.localService.setItem("auto_login", "false");
              }
              this.localService.setItem("user_token", token.tokens[0]);
              this.initFromToken(token.tokens[0]);
            }
          }
        },
        error: () => {
          this.statusService.setLoginStatus("Something has gone wrong. Please try again at a later point");
          this.router.navigate(['/login']);
        }
      })
  }

    checkTokens(tokens: string[]): string {
      var local_token: string | null = this.localService.getItem("user_token");
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
