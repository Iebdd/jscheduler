import { StatusPipe } from '../Pipes/status.pipe';

describe('StatusPipe', () => {
  it('create an instance', () => {
    const pipe = new StatusPipe();
    expect(pipe).toBeTruthy();
  });
});
