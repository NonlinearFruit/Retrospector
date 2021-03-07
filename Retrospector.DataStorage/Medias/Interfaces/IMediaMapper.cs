using Retrospector.DataStorage.Medias.Entities;

namespace Retrospector.DataStorage.Medias.Interfaces
{
    public interface IMediaMapper
    {
        Core.Crud.Models.Media ToModel(MediaEntity entity);
        MediaEntity ToEntity(Core.Crud.Models.Media model);
    }
}