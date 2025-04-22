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


  post(URI: string, params: HttpParams, token?: string): Observable<HttpResponse<string>> {
    var options;
    if(token != undefined) {
      options = {
        headers: new HttpHeaders()
                    .append("Authorization", `Bearer ${token}`)
                    .append("Content-Type", "application/x-www-form-urlencoded"),
        observe: 'response' as const
      };
    } else {
      options = {
        headers: new HttpHeaders().append("Content-Type", "application/x-www-form-urlencoded"),
        observe: 'response' as const
      };
    }
    return this._http.post<string>(URI, params, options);
  }

  patch(URI: string, params: HttpParams, token?: string): Observable<HttpResponse<string>> {
    var options;
    if (token != undefined) {
      options = {
        headers: new HttpHeaders()
                    .append("Authorization", `Bearer ${token}`)
                    .append("Content-Type", "application/x-www-form-urlencoded"),
        observe: `response` as const
      };
    } else {
      options = {
        observe: 'response' as const
      };
    }
    return this._http.patch<string>(URI, params, options);
  }

  // Create
  
  addUser(role: number, firstName: string, lastName: string, password: string, email: string): Observable<HttpResponse<string>> {
    const params = new HttpParams()
      .append("role", role)
      .append("firstName", firstName)
      .append("lastName", lastName)
      .append("password", password)
      .append("email", email);
    return this.post(`${this._URI}/add/user`, params);    //Returns UserToken object
  }

  addToken(email: string, password: string): Observable<HttpResponse<string>> {
    const params = new HttpParams()
      .append("email", email)
      .append("password", password);
    return this.post(`${this._URI}/add/token`, params);   //Returns a UserToken object
  }

  addCourse(courseName: string, token: string): Observable<HttpResponse<string>> {
    const params = new HttpParams()
      .append("courseName", courseName);
    return this.post(`${this._URI}/add/course`, params, token);   //Returns Course object
  }

  addRoom(roomName: string, token: string): Observable<HttpResponse<string>> {
    let values = {"roomName": roomName};
    const params = new HttpParams()
      .append("roomName", roomName);
    return this.post(`${this._URI}/add/room`, params, token);     //Returns Room object
  }

  addBooking(course_id: string, room_id: string, start: string, end: string, token: string, preference: boolean): Observable<HttpResponse<string>> {
    let values = {"course_id": course_id, "room_id": room_id, "start": start, "end": end};
    const params = new HttpParams()
      .append("course_id", course_id)
      .append("room_id", room_id)
      .append("start", start)
      .append("end", end);
    if (preference) {
      return this.post(`${this._URI}/add/preference`, params, token);   //Returns Booking object
    } else {
      return this.post(`${this._URI}/add/booking`, params, token);      //Returns Booking object
    }
  }
  
  addInscription(user_id: string, course_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"user_id": user_id, "course_id": course_id};
    const params = new HttpParams()
      .append("user_id", user_id)
      .append("course_id", course_id);
    return this.post(`${this._URI}/add/inscription`, params, token);    //Returns UserBooking object
  }

  //Delete

  removeInscription(user_id: string, course_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"user_id": user_id, "course_id": course_id};
    const params = new HttpParams()
      .append("user_id", user_id)
      .append("course_id", course_id);
    return this.post(`${this._URI}/remove/inscription`, params, token); //Returns string
  }

  removeBooking(booking_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"booking_id": booking_id};
    const params = new HttpParams()
      .append("booking_id", booking_id);
    return this.post(`${this._URI}/remove/booking`, params, token);     //Returns number
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

  getAllBookings(token: string): Observable<HttpResponse<Object>> {
    return this.get(`${this._URI}/read/bookings`, token);   //Returns Booking Array
  }

  getTimeline(room_id: string, date: string, token: string): Observable<HttpResponse<string>> {
    return this.get(`${this._URI}/read/timeline/${room_id}/${date}`, token); //Returns string array
  }

  //Update

  updatePasswordViaPassword(old_password: string, new_password: string, user_id: string): Observable<HttpResponse<string>> {
    let values = {"old_password": old_password, "new_password": new_password, "user_id": user_id};
    const params = new HttpParams()
      .append("old_password", old_password)
      .append("new_password", new_password)
      .append("user_id", user_id);
    return this.patch(`${this._URI}/update/password`, params);
  }

  updatePasswordViaToken(new_password: string, user_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"new_password": new_password, "user_id": user_id};
    const params = new HttpParams()
      .append("new_password", new_password)
      .append("user_id", user_id);
    return this.patch(`${this._URI}/update/password`, params, token);
  }

  updateBookingStatus(new_status: Status, booking_id: string, token: string): Observable<HttpResponse<string>> {
    let values = {"new_status": new_status, "booking_id": booking_id};
    const params = new HttpParams()
      .append("new_status", new_status)
      .append("booking_id", booking_id);
    return this.patch(`${this._URI}/update/bookingStatus`, params, token);    //Returns number
  }

  updateToken(user_id: string, token: string): Observable<HttpResponse<string>> {
    const params = new HttpParams().append("user_id", user_id);
    return this.patch(`${this._URI}/update/token`, params, token);    //Returns boolean
  }

  //Verify

  verifyLogin(email: string, password: string): Observable<HttpResponse<string>> {
    const params = new HttpParams()
      .append("email", email)
      .append("password", password);
    return this.post(`${this._URI}/verify/login`, params);       //Returns UserToken object
  }

  verifyToken(token: string): Observable<HttpResponse<string>> {  //Returns User object
    return this.get(`${this._URI}/verify/token`, token);
  }

  verifyEmail(email: string): Observable<HttpResponse<string>> {  //Returns boolean
    return this.get(`${this._URI}/verify/available/email/${email}`);
  }
}
