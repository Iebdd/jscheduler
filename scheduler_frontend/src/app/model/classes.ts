
export class UserToken {
    private _status?: number;
    private _userId?: string;
    private _tokens?: string[];
    private _check: boolean;

    constructor(token: UserToken) {
        this._userId = token._userId;
        this._check = token._check;
        this._tokens = token._tokens;
    }

    get UserId(): string | undefined {
        return this._userId;
    }

    get Tokens(): string[] | undefined {
        return this._tokens;
    }

    get Check(): boolean {
        return this._check;
    }

    get Status(): number | undefined {
        return this._status;
    }

    set Status(status: number) {
        this._status = status;
    }

}

export class UserBooking {
    private _status?: number;
    private _room_conflicts: string[];
    private _time_conflicts: string[];
    private _booking_id: string;

    constructor(booking: UserBooking) {
        this._room_conflicts = booking._room_conflicts;
        this._time_conflicts = booking._time_conflicts;
        this._booking_id = booking._booking_id;
    }

    get Room_Conflicts(): string[] {
        return this._room_conflicts;
    }

    get Time_Conflicts(): string[] {
        return this._time_conflicts;
    }

    get Booking_Id(): string {
        return this._booking_id;
    }

    get Status(): number | undefined{
        return this._status;
    }

    set Status(status: number) {
        this._status = status;
    }
}

export const enum Status {
    Set,
    Planned,
    Preference
}

export interface User {
    _role: number;
    _first_name: string;
    _last_name: string;
    _password: string;
    _email: string;
}

export interface Course {
    _course_id: string;
    _course_name: string;
}

export interface Booking {
    _bookings_id: string;
    _room_id: string;
    _course_id: string;
    _start: string;
    _end: string;
    _status: Status;
}

export interface Room {
    _room_id: string;
    _start: string;
    _end: string;
    _room_name: string;
}

export interface Inscription {
    _user_id: string;
    _course_id: string;
}