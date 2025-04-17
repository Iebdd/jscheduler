import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { UserBooking, UserToken } from '../model/classes';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoadDataService {

  constructor() { }
  private _http = inject(HttpClient);
  
  get<T>(URI: string, token?: string, path?: string): Observable<HttpResponse<T>> {
      if(token == undefined) {
        return this._http.get<T>(URI, {observe: 'response'});
      } else {
        return this._http.get<T>(URI, {
            headers: {"Authorization": `Bearer ${token}`}, 
            observe: 'response'
          })
      }
  }

  post<T>(URI: string, header: string): Observable<T> {
    return this._http.post<T>(URI, header);
  }
}
