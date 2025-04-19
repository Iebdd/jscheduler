
export interface UserToken {
    status?: number;
    userId?: string;
    tokens?: string[];
    check: boolean;
}

export interface UserBooking {
    status?: number;
    room_conflicts: string[];
    time_conflicts: string[];
    booking_id: string;
}

export const enum Status {
    Set,
    Planned,
    Preference
}

export interface User {
    role:      number;
    firstName: string;
    lastName:  string;
    email:     string;
    userId:    string;
}

export interface Course {
    id: string;
    courseName: string;
}

export interface Booking {
    bookings_id: string;
    room: Room;
    course: Course;
    start: string;
    end: string;
    status: Status;
}

export interface Room {
    id: string;
    start: string;
    end: string;
    roomName: string;
}

export interface Inscription {
    user_id: string;
    course_id: string;
}