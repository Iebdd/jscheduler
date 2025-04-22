import { Pipe, PipeTransform } from '@angular/core';
import { Booking, Course } from '../model/interfaces';

@Pipe({
  name: 'courseName'
})
export class CourseNamePipe implements PipeTransform {


  transform(id: string, bookings: string, courses: Course[]): string {
    var obj_bookings: Booking[] = JSON.parse(bookings);
    var result: string = "";
    for(var booking of obj_bookings) {
      if(booking.bookings_id == id) {
        for(var course of courses) {
          if(course.id == booking.course_id) {
            return course.courseName;
          }
        }
        }
    }
    return "";
  }

}
