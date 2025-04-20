import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatusService {

  constructor() { }

    private _loading_status = new BehaviorSubject<string>("");
    private _login_status = new BehaviorSubject<string>("");
  
    public setLoadingStatus(status: string) {
      this._loading_status.next(status);
    }

    public getLoadingStatus(): Observable<string> {
      return this._loading_status.asObservable();
    }

    public setLoginStatus(status: string) {
      this._login_status.next(status);
    }

    public getLoginStatus(): Observable<string> {
      return this._login_status.asObservable();
    }
}
