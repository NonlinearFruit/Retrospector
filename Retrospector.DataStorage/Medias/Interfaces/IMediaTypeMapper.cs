using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Medias.Entities;

namespace Retrospector.DataStorage.Medias.Interfaces
{
    public interface IMediaTypeMapper
    {
        MediaType ToModel(MediaTypeEntity entity);
        MediaTypeEntity ToEntity(MediaType model);
    }
}