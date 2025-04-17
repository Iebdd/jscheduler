
export class UserToken {
    private userId?: string;
    private tokens?: string[];
    private check: boolean;

    constructor(token: UserToken) {
        this.userId = token.userId;
        this.check = token.check;
        this.tokens = token.tokens;
    }

    get UserId(): string | undefined {
        return this.userId;
    }

    get Tokens(): string[] | undefined {
        return this.tokens;
    }

    get Check(): boolean {
        return this.check;
    }

}

export class UserBooking {
    private room_conflicts: string[];
    private time_conflicts: string[];
    private booking_id: string;

    constructor(booking: UserBooking) {
        this.room_conflicts = booking.room_conflicts;
        this.time_conflicts = booking.time_conflicts;
        this.booking_id = booking.booking_id;
    }

    get Room_Conflicts(): string[] {
        return this.room_conflicts;
    }

    get Time_Conflicts(): string[] {
        return this.time_conflicts;
    }

    get Booking_Id(): string {
        return this.booking_id;
    }

}