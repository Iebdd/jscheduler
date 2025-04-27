
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
    user_id:    string;
}

export interface Booking {
  bookings_id: string;
  room_id:   string;
  course_id: string;
  start:  Date;
  end:    Date;
  status: string;
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

export interface Mouseover {
  booking_id: string;
  room: number;
  index: number;
}

export interface Info {
  booking_id: string;
  room: number;
  info: boolean;
  start: string;
  end: string;
  index: number;
}