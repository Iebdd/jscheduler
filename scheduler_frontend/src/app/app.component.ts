import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TopBarComponent } from './top-bar/top-bar.component';
import { MainBodyComponent } from "./main-body/main-body.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TopBarComponent, MainBodyComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'scheduler_frontend';
}
