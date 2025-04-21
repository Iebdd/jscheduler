import { Pipe, PipeTransform } from '@angular/core';
import { Booking, Course } from '../model/interfaces';

@Pipe({
  name: 'courseName'
})
export class CourseNamePipe implements PipeTransform {


  transform(id: string, bookings: Booking[]): string {
    var result: string = "";
    console.log("Input: " + )
    for(var booking of bookings) {
      if(booking.id == id) {
       result = booking.course.courseName;
      }
    }
    if(result.length != 0) {
      return result;
    } else {
      return '';
    }
  }

}
