import { Component, inject, OnInit } from '@angular/core';
import { Feedback, LoginStates as States } from '../model/enums';
import { User, UserToken } from '../model/interfaces';
import { FormsModule } from '@angular/forms';
import { StatusService } from '../Services/status.service';
import { LoadDataService } from '../Services/load-data.service';
import { UserService } from '../Services/user.service';
import { LocalService } from '../Services/local.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {

  private statusService: StatusService = inject(StatusService);
  private loadDataService: LoadDataService = inject(LoadDataService);
  private userService: UserService = inject(UserService);
  private localService: LocalService = inject(LocalService);

  protected _state: States = States.Login;
  protected _states = States;
  protected _feedback = Feedback;
  protected _login_status: string = "";
  protected _login_mood: Feedback = Feedback.Neutral;
  private _taken_email: boolean = false;

  protected _register_firstName: string = "";
  protected _register_lastName: string = "";
  protected _register_email: string = "";
  protected _register_password: string = "";
  protected _login_email: string = "";
  protected _login_password: string = "";
  protected _remember: boolean = false;

  setLoginState(state: States): void {
    this.clearInput();
    this._taken_email = false;
    this._state = state;
  }

  signUp(): void {
    if(this._register_firstName.length < 1 || this._register_lastName.length < 1 ||
       this._register_email.length < 1 || this._register_password.length < 1) {
        this.setStatus("Please fill out all fields", Feedback.Invalid);
        return;
    } else if (!this.validEmail(this._register_email)) {
      this.setStatus("Please enter a valid email address", Feedback.Invalid);
      return;
    } else if (this._taken_email) {
      this.setStatus("Please choose a different email address", Feedback.Invalid);
      return;
    }
    this.loadDataService.addUser(0, this._register_firstName, this._register_lastName, this._register_password, this._register_email)
      .subscribe({
        next: (value) => {
          if (value.ok && value.body != null) {
            var user_token: UserToken = JSON.parse(JSON.stringify(value.body));
            if(user_token.tokens != null) {
              this.localService.setItem("user_token", user_token.tokens[0]);
              this.userService.initFromToken(user_token.tokens[0]);
            }
          }
        },
        error: (value) => {
          this.setStatus("Something has gone wrong. Please try again at a later point", Feedback.Invalid);
        }
      })
  }

  signIn(): void {
    if(this._login_email.length < 1 || this._login_password.length < 1) {
      this.setStatus("Please fill out all field", Feedback.Invalid);
      return;
    } else if (!this.validEmail(this._login_email)) {
      this.setStatus("Please enter a valid email address", Feedback.Invalid);
      return;
    }
    this.loadDataService.verifyLogin(this._login_email, this._login_password)
      .subscribe({
        next: (value) => {
          if(value.body != null) {
            var token: UserToken = JSON.parse(JSON.stringify(value.body));
            if(token.check && token.tokens != null) {
              var local_token: string = this.userService.checkTokens(token.tokens);
              if(local_token.length > 0 && token.userId != null) {
                if(this._remember) {
                  this.localService.setItem("auto_login", "true");
                } else {
                  this.localService.setItem("auto_login", "false");
                }
                this.loadDataService.updateToken(token.userId, local_token);
                this.userService.initFromToken(local_token);
              } else if(this._remember) {
                this.userService.initWithoutToken(this._login_email, this._login_password, true);
              } else {
                this.userService.initWithoutToken(this._login_email, this._login_password, false);
              }
            } else {
              if(token.tokens != null) {
                this.localService.setItem("user_token", token.tokens[0]);
                this.userService.initFromToken(token.tokens[0]);
              }
            }
          }
        },
        error: () => {
          this.setStatus("Something has gone wrong. Please try again at a later point", Feedback.Invalid);
        }
      })
  }

  setStatusColor(): string {
    switch(this._login_mood) {
      case Feedback.Valid:
        return "green";
      case Feedback.Invalid:
        return "red";
      default:
        return "black";
    }
  }

  setStatus(status: string, mood: Feedback): void {
    this.statusService.setLoginStatus(status);
    this._login_mood = mood;
  }

  setNeutralMood(): void {
    this.setStatus("", Feedback.Neutral);
  }

  clearInput(): void {
    this.setNeutralMood();
    if(this._state == States.Login) {
      this._login_email = "";
      this._login_password = "";
    } else if (this._state == States.Register) {
      this._register_email = "";
      this._register_password = "";
      this._register_lastName = "";
      this._register_firstName = "";
    }
  }

  newEmailEvent(event: any) {                                 
     var email: string = event.target.value;  
    if(this.validEmail(email)) {
      this.loadDataService.verifyEmail(email)
        .subscribe(result => {
          if(result.body != null && JSON.parse(result.body) == true) {
            this.setStatus("Please choose a different email address", Feedback.Invalid);
            this._taken_email = true;
          } else {
            this.setStatus("This email is still available", Feedback.Valid);
            this._taken_email = false;
          }

        })       
    } else if(this._login_mood != Feedback.Neutral) {
      this.setNeutralMood();
      this._taken_email = false;
    }
  }

  validEmail(email: string): boolean {
    if(email.match(/(?!(^[.-].*|[^@]*\.@|.*\.{2,}.*)|^.{254}.)([a-zA-Z0-9!#$%&'*+\/=?^_`{|}~.-]+@)(?!-.*|.*-\.)([a-zA-Z0-9-]{1,63}\.)+[a-zA-Z]{2,15}/g)) {
      return true;
    } else {                   //  Email regex pattern taken from this StackOverflow answer: 
      return false;            //  https://stackoverflow.com/questions/27000681/how-to-verify-form-input-using-html5-input-verification/27000682#27000682
    }
  }


  getLoginStatus() {
    this.statusService.getLoginStatus()
      .subscribe(status => this._login_status = status);
  }

  ngOnInit() {
    this.getLoginStatus();
  }
}
