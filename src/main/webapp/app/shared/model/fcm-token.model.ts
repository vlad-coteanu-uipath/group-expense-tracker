export interface IFCMToken {
  id?: number;
  token?: string;
  appUserId?: number;
}

export class FCMToken implements IFCMToken {
  constructor(public id?: number, public token?: string, public appUserId?: number) {}
}
