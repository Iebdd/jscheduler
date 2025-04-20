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
  private _bookings = new BehaviorSubject<Booking[]>([]);

  getUserData(): Observable<User> {
    return this._userData.asObservable();
  }

  getCourses(): Observable<Course[]> {
    return this._courses.asObservable();
  }

  getRooms(): Observable<Room[]> {
    return this._rooms.asObservable();
  }

  getBookings(): Observable<Booking[]> {
    return this._bookings.asObservable();
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

  setBookings(bookings: Booking[]): void {
    this._bookings.next(bookings);
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

  requestRooms(): void {
    this.loadDataService.getRooms()
    .subscribe({
      next: (value) => {
        if(value.body != null) {
            this.statusService.setLoadingStatus("Done");
            this.setRooms(JSON.parse(JSON.stringify(value.body)));
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
            this.setBookings(JSON.parse(JSON.stringify(value.body)));
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
            this.setBookings(Parse.toBooking(JSON.stringify(value.body)));
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
    this.requestRooms();
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
