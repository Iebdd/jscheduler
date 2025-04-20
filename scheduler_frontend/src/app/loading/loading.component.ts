import { Component, inject, OnInit } from '@angular/core';
import { StatusService } from '../Services/status.service';

@Component({
  selector: 'app-loading',
  imports: [],
  templateUrl: './loading.component.html',
  styleUrl: './loading.component.scss'
})
export class LoadingComponent implements OnInit {

  private statusService: StatusService = inject(StatusService);

  protected _loading_status: string = "";

  getLoadingStatus(): void {
    this.statusService.getLoadingStatus()
      .subscribe(status => this._loading_status = status);
  }

  ngOnInit(): void {
    this.getLoadingStatus();
  }

}
