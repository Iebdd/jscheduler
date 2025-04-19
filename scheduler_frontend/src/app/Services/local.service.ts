import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalService {

  constructor() { }

  getItem(key: string): string | null {
    return localStorage.getItem(key);
  }

  setItem(key: string, item: string): void {
    localStorage.setItem(key, item);
  }

  deleteItem(key: string): void {
    localStorage.removeItem(key);
  }

  exists(key: string): boolean {
    return (typeof localStorage.getItem(key) == 'string') ? true : false;
  }
}
