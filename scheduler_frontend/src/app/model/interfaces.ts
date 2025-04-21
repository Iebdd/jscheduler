
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

export interface Booking {
  room:   Room;
  course: Course;
  start:  Date;
  end:    Date;
  status: string;
  id:     string;
}

export interface Course {
  courseName: string;
  id:         string;
}

export interface Room {
  start:    string;
  end:      string;
  id:       string;
  roomName: string;
}

export interface Inscription {
    user_id: string;
    course_id: string;
}

export interface Segment {
  booking_id: string;
  status: number;
  empty: boolean;
}