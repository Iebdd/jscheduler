import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoadDataService {

  constructor() { }
  private http = inject(HttpClient);
  
  get
}
