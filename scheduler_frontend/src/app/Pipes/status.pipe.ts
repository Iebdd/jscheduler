import { Pipe, PipeTransform } from '@angular/core';
import { InfoStates } from '../model/enums';
import { Status } from '../model/interfaces';

@Pipe({
  name: 'status'
})
export class StatusPipe implements PipeTransform {

  transform(status: string): number {
    switch(status) {
      case 'Set':
        return 0;
      case 'Planned':
        return 1;
      case 'Preference':
        return 2;
      default:
        return -1;
    }
  }

}
