import { RoomNamePipe } from '../Pipes/room-name.pipe';

describe('RoomNamePipe', () => {
  it('create an instance', () => {
    const pipe = new RoomNamePipe();
    expect(pipe).toBeTruthy();
  });
});
