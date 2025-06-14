import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

/**
 * Keeps the status of different objects
 */
@Injectable({
  providedIn: 'root'
})
export class StatusService {

  constructor() { }

    private _loading_status = new BehaviorSubject<string>("");
    private _login_status = new BehaviorSubject<string>("");
    private _dashboard_status = new BehaviorSubject<string>("");
  
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

    public getDashboardStatus(): Observable<string> {
      return this._dashboard_status.asObservable();
    }

    public setDashboardStatus(status: string): void {
      this._dashboard_status.next(status);
    }
}
