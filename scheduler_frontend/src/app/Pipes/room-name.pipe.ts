import { Pipe, PipeTransform } from '@angular/core';
import { Booking, Room } from '../model/interfaces';

@Pipe({
  name: 'roomName'
})
export class RoomNamePipe implements PipeTransform {

  transform(booking_id: string, bookings: string, rooms: Room[]): string {
    var obj_bookings: Booking[] = JSON.parse(bookings);
    for(var booking of obj_bookings) {
      if(booking.bookings_id == booking_id) {
        for(var room of rooms) {
          if(room.id == booking.room_id) {
            return room.roomName;
          }
        }
        }
    }
    return "";
  }

}
