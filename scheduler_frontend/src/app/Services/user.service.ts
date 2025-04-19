import { inject, Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Booking, Course, Room, User } from '../model/interfaces';
import { LoadDataService } from './load-data.service';
import { LocalService } from './local.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService implements OnDestroy {

  constructor() {}

  private loadDataService: LoadDataService = inject(LoadDataService);
  private localService: LocalService = inject(LocalService);
  private router: Router =  inject(Router);

  private destroyed = new Subject<void>;
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
            console.log(value.body);
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
            this.setBookings(JSON.parse(JSON.stringify(value.body)));
            this.router.navigate(['/dashboard']);
          }
        }
      })
  }


  requestBaseData(user: User, user_token: string): void {
    this.setUserData(user);
    this.requestCourses();
    this.requestRooms();
    if (user.role == 0) {
      this.requestUserBookings(user.userId, user_token);
    } else {
      this.requestBookings(user_token);
    }
  }

  ngOnDestroy(): void {
    this.destroyed.next();
    this.destroyed.complete();
  }

  initUser(): void {
    this.router.navigate(['/loading']);
      var user_token: string | null = this.localService.getItem("user_token");
      if(user_token != null) {
        this.loadDataService.verifyToken(user_token)
          .subscribe({
            next: (value) => {
              if(value.ok && value.body != null && user_token != null) {  //Double type guard because the compiler doesn't pick up the inner one
                this.requestBaseData(JSON.parse(JSON.stringify(value.body)), user_token);
              } else {
                this.localService.deleteItem("user_token");
                this.router.navigate(['/login']);
              }
            },
            error: (value) => {
              this.localService.deleteItem("user_token");
              this.router.navigate(['/login']);
            }
          })
      } else {
        this.router.navigate(['/login']);
      }
    }
}
