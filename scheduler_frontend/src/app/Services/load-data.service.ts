import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import { Booking, Course, Room, Status, User, UserBooking, UserToken} from '../model/interfaces';
import { Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoadDataService {

  constructor() { }
  private _http: HttpClient = inject(HttpClient);
  private _URI: String = "http://localhost:8080";
  
  get(URI: string, token?: string): Observable<HttpResponse<string>> {
      if(token == undefined) {
        return this._http.get<string>(URI, {observe: 'response'});
      } else {
          return this._http.get<string>(URI, {
            headers: {"Authorization": `Bearer ${token}`}, 
            observe: 'response'
          })
      }
  }

  post(URI: string, values: Object, token?: string): Observable<HttpResponse<string>> {
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
    return this._http.post<string>(URI, body, options);
  }

  patch(URI: string, values: Object, token?: string): Observable<HttpResponse<string>> {
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
    return this._http.patch<string>(URI, body, options);
  }

  // Create
  
  addUser(role: number, firstName: string, lastName: string, password: string, email: string): Observable<HttpResponse<string>> {
    let values = {"role": role, "firstName": firstName, "lastName": lastName, "password": password, "email": email};
    return this.post(`${this._URI}/add/user`, values);    //Returns UserToken object
  }

  addCourse(courseName: string, token: string): Observable<HttpResponse<string>> {
    let values = {"courseName": courseName};
    return this.post(`${this._URI}/add/course`, values, token);   //Returns Course object
  }

  addRoom(roomName: string, token: string): Observable<HttpResponse<string>> {
    let values = {"roomName": roomName};
    return this.post(`${this._URI}/add/room`, values, token);     //Returns Room object
  }

  addBooking(course_id: string, room_id: string, start: string, end: string, token: string, preference: boolean): Observable<HttpResponse<string>> {
    let values = {"course_id": course_id, "room_id": room_id, "start": start, "end": end};
    if (preference) {
      return this.post(`${this._URI}/add/preference`, values, token);   //Returns Booking object
    } else {
      return this.post(`${this._URI}/add/booking`, values, token);      //Returns Booking object
    }
  }
  
  addInscription(user_id: string, course_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"user_id": user_id, "course_id": course_id};
    return this.post(`${this._URI}/add/inscription`, values, token);    //Returns UserBooking object
  }

  //Delete

  removeInscription(user_id: string, course_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"user_id": user_id, "course_id": course_id};
    return this.post(`${this._URI}/remove/inscription`, values, token); //Returns string
  }

  removeBooking(booking_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"booking_id": booking_id};
    return this.post(`${this._URI}/remove/booking`, values, token);     //Returns number
  }

  //Read

  getRooms(): Observable<HttpResponse<string>> {
    return this.get(`${this._URI}/read/rooms`);       //Returns Rooms array
  }

  getCourses(): Observable<HttpResponse<string>> {
    return this.get(`${this._URI}/read/courses`);     //Return Course array
  }

  getRoomIdByName(name: string): Observable<HttpResponse<string>> {     //Returns UUID
    return this.get(`${this._URI}/read/room/${name}`);
  }

  getCourseIdByName(name: string): Observable<HttpResponse<string>> {   //Returns UUID
    return this.get(`${this._URI}/read/course/${name}`);
  }

  getCoursesByRoom(room_id: string): Observable<HttpResponse<string>> { // Returns Booking object
    return this.get(`${this._URI}/read/roomBookings/${room_id}`);
  }

  getCoursesByRoomByDay(room_id: string, day: string): Observable<HttpResponse<string>> { // Returns Booking object
    return this.get(`${this._URI}/read/roomBookings/${room_id}/${day}`);
  }

  getUserBookings(user_id: string, token: string): Observable<HttpResponse<string>> {
    return this.get(`${this._URI}/read/userBookings/${user_id}`, token);  //Returns Booking array
  }

  getAllBookings(token: string): Observable<HttpResponse<string>> {
    return this.get(`${this._URI}/read/bookings`, token);   //Returns Booking Array
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

  updateBookingStatus(new_status: Status, booking_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"new_status": new_status, "booking_id": booking_id};
    return this.patch(`${this._URI}/update/bookingStatus`, values, token);    //Returns number
  }

  updateToken(user_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"user_id": user_id};
    return this.patch(`${this._URI}/update/token`, values, token);    //Returns boolean
  }

  //Verify

  verifyLogin(email: string, password: string): Observable<HttpResponse<string>> {
    let values = {"email": email, "password": password};
    return this.post(`${this._URI}/verify/login`, values);       //Returns UserToken object
  }

  verifyToken(token: string): Observable<HttpResponse<string>> {  //Returns User object
    return this.get(`${this._URI}/verify/token`, token);
  }
}
