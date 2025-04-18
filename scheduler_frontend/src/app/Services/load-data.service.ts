import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpResponse, HttpResponseBase } from '@angular/common/http';
import { Booking, Course, Room, Status, UserBooking, UserToken} from '../model/classes';
import { Observable, of } from 'rxjs';
import { MainBodyComponent } from '../main-body/main-body.component';

@Injectable({
  providedIn: 'root'
})
export class LoadDataService {

  constructor() { }
  private _http: HttpClient = inject(HttpClient);
  private _URI: String = "https://localhost:8080";
  
  get<T>(URI: string, token?: string): Observable<HttpResponse<T>> {
      if(token == undefined) {
        return this._http.get<T>(URI, {observe: 'response'});
      } else {
          return this._http.get<T>(URI, {
            headers: {"Authorization": `Bearer ${token}`}, 
            observe: 'response'
          })
      }
  }

  post<T>(URI: string, values: Object, token?: string): Observable<HttpResponse<T>> {
    var body: HttpParams = new HttpParams(values);
    var options;
    if(token != undefined) {
      options = {
        headers: new HttpHeaders().append("Authorization", `Bearer ${token}`),
        observe: 'response' as const
      };
    } else {
      options = {
        observe: 'response' as const
      };
    }
    return this._http.post<T>(URI, body, options);
  }

  patch<T>(URI: string, values: Object, token?: string): Observable<HttpResponse<T>> {
    var body: HttpParams = new HttpParams(values);
    var options;
    if (token != undefined) {
      options = {
        headers: new HttpHeaders().append("Authorization", `Bearer ${token}`),
        observe: `response` as const
      };
    } else {
      options = {
        observe: 'response' as const
      };
    }
    return this._http.patch<T>(URI, body, options);
  }

  // Create
  
  addUser(role: number, firstName: string, lastName: string, password: string, email: string): Observable<HttpResponse<UserToken>> {
    let values = {"role": role, "firstName": firstName, "lastName": lastName, "password": password, "email": email};
    return this.post(`${this._URI}/add/user`, values);
  }

  addCourse(courseName: string, token: string): Observable<HttpResponse<Course>> {
    let values = {"courseName": courseName};
    return this.post(`${this._URI}/add/course`, values, token);
  }

  addRoom(roomName: string, token: string): Observable<HttpResponse<Room>> {
    let values = {"roomName": roomName};
    return this.post(`${this._URI}/add/room`, values, token);
  }

  addBooking(course_id: string, room_id: string, start: string, end: string, token: string, preference: boolean): Observable<HttpResponse<Booking>> {
    let values = {"course_id": course_id, "room_id": room_id, "start": start, "end": end};
    if (preference) {
      return this.post(`${this._URI}/add/preference`, values, token);
    } else {
      return this.post(`${this._URI}/add/booking`, values, token);
    }
  }
  
  addInscription(user_id: string, course_id: string, token: string): Observable<HttpResponse<UserBooking>> {
    let values = {"user_id": user_id, "course_id": course_id};
    return this.post(`${this._URI}/add/inscription`, values, token);
  }

  //Delete

  removeInscription(user_id: string, course_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"user_id": user_id, "course_id": course_id};
    return this.post(`${this._URI}/remove/inscription`, values, token);
  }

  removeBooking(booking_id: string, token: string): Observable<HttpResponse<number>> {
    let values = {"booking_id": booking_id};
    return this.post(`${this._URI}/remove/booking`, values, token);
  }

  //Read

  getCourseIdByName(name: string): Observable<HttpResponse<string>> {
    return this.get(`${this._URI}/read/course/${name}`);
  }

  getRoomIdByName(name: string): Observable<HttpResponse<string>> {
    return this.get(`${this._URI}/read/room/${name}`);
  }

  getUserBookings(user_id: string, token: string): Observable<HttpResponse<Booking[]>> {
    return this.get(`${this._URI}/read/userBookings/${user_id}`, token);
  }

  getCourses(): Observable<HttpResponse<Course[]>> {
    return this.get(`${this._URI}/read/courses`);
  }

  getRooms(): Observable<HttpResponse<Room[]>> {
    return this.get(`${this._URI}/read/rooms`);
  }

  //Update

  updatePasswordViaPassword(old_password: string, new_password: string, user_id: string): Observable<HttpResponse<string>> {
    let values = {"old_password": old_password, "new_password": new_password, "user_id": user_id};
    return this.patch(`${this._URI}/update/password`, values);
  }

  updatePasswordViaToken(new_password: string, user_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"new_password": new_password, "user_id": user_id};
    return this.patch(`${this._URI}/update/password`, values, token);
  }

  updateBookingStatus(new_status: Status, booking_id: string, token: string): Observable<HttpResponse<number>> {
    let values = {"new_status": new_status, "booking_id": booking_id};
    return this.patch(`${this._URI}/update/bookingStatus`, values, token);
  }

  updateToken(user_id: string, token: string): Observable<HttpResponse<boolean>> {
    let values = {"user_id": user_id};
    return this.patch(`${this._URI}/update/token`, values, token);
  }

  //Verify

  verifyLogin(email: string, password: string): Observable<HttpResponse<UserToken>> {
    let values = {"email": email, "password": password};
    return this.post(`${this._URI}/verify/login`, values);
  }
}
